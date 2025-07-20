import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, cross_val_score, GridSearchCV
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier, VotingClassifier
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix
from sklearn.naive_bayes import MultinomialNB
from sklearn.linear_model import LogisticRegression
from sklearn.svm import SVC
import re
from datetime import datetime
import seaborn as sns
import matplotlib.pyplot as plt
import pickle
import os

class ImprovedTransactionClassifier:
    def __init__(self):
        self.vectorizer = None
        self.scaler = StandardScaler()
        self.label_encoder = LabelEncoder()
        self.model = None
        self.merchant_category_map = {}
        
    def load_and_preprocess_data(self, file_path):
        """Load and preprocess the data with improved cleaning"""
        df = pd.read_csv(file_path)
        
        # Clean data
        df = df.dropna(subset=['Category'])
        df = df[df['Category'].str.strip() != '']
        
        # Clean categories - standardize similar categories
        df['Category'] = df['Category'].str.strip().str.title()
        
        # Fix common category variations
        category_mapping = {
            'Food Order': 'Food order',
            'Foodorder': 'Food order',
            'Household Items': 'Household items',
            'Utilty Bills': 'Utility bills',
            'Utility Bills': 'Utility bills',
            'Services': 'Service',
            'Deposit': 'Deposits'
        }
        df['Category'] = df['Category'].replace(category_mapping)
        
        # Convert amounts
        df['Withdrawal Amt.'] = pd.to_numeric(df['Withdrawal Amt.'], errors='coerce').fillna(0)
        df['Deposit Amt.'] = pd.to_numeric(df['Deposit Amt.'], errors='coerce').fillna(0)
        
        # Parse dates
        df['Date'] = pd.to_datetime(df['Date'], format='%d/%m/%y', errors='coerce')
        
        print(f"Categories found: {df['Category'].value_counts()}")
        
        return df
    
    def extract_advanced_features(self, df):
        """Extract more sophisticated features"""
        features = []
        
        # Build merchant-category mapping from training data
        for _, row in df.iterrows():
            merchant = self._extract_merchant_name(row['Narration'])
            if merchant and 'Category' in row:
                if merchant not in self.merchant_category_map:
                    self.merchant_category_map[merchant] = {}
                cat = row['Category']
                self.merchant_category_map[merchant][cat] = self.merchant_category_map[merchant].get(cat, 0) + 1
        
        # Extract features for each transaction
        for _, row in df.iterrows():
            narration = str(row['Narration']).upper()
            amount = float(row['Withdrawal Amt.'])
            deposit = float(row.get('Deposit Amt.', 0))
            date = row.get('Date')
            
            # Basic features
            feature_dict = {
                'amount': amount,
                'amount_log': np.log1p(amount),  # Log transform for better distribution
                'is_deposit': 1 if deposit > 0 else 0,
                'amount_hundreds': amount // 100,
                'amount_thousands': amount // 1000,
            }
            
            # Date features
            if pd.notna(date):
                feature_dict.update({
                    'day_of_week': date.dayofweek,
                    'day_of_month': date.day,
                    'is_weekend': 1 if date.dayofweek >= 5 else 0,
                    'is_month_start': 1 if date.day <= 5 else 0,
                    'is_month_end': 1 if date.day >= 25 else 0,
                })
            
            # Enhanced keyword features with weights
            keywords = {
                'food': ['ZOMATO', 'SWIGGY', 'FOOD', 'RESTAURANT', 'TUMMY', 'SARVAJITH FOODS'],
                'grocery': ['GROCERY', 'VEGETABLE', 'ZEPTON', 'STORE', 'AMWAY', 'FRESH'],
                'utility': ['TANGEDCO', 'BILL', 'METROPOLITAN', 'WATER', 'ELECTRICITY', 'CHENNAI METRO'],
                'emi': ['EMI', 'LOAN', 'COLLECTION', 'HOUSING', 'FINANCE', 'INSTALLMENT'],
                'medical': ['MEDICAL', 'PHARMACY', 'PHARMA', 'HOSPITAL', 'DOCTOR', 'APOLLO', 'HEALTH'],
                'service': ['URBANCLAP', 'URBAN COMPANY', 'SERVICE', 'YOUTUBE', 'REPAIR', 'MAINTENANCE'],
                'transport': ['OLA', 'UBER', 'TAXI', 'RAPIDO', 'TRANSPORT', 'TRAVEL'],
                'shopping': ['AMAZON', 'FLIPKART', 'MYNTRA', 'BRAND', 'FASHION', 'CLOTHING'],
                'deposit': ['RD INSTALLMENT', 'DEPOSIT', 'SAVINGS', 'RECURRING'],
            }
            
            for category, words in keywords.items():
                feature_dict[f'has_{category}'] = 1 if any(word in narration for word in words) else 0
                # Count occurrences
                feature_dict[f'{category}_count'] = sum(1 for word in words if word in narration)
            
            # Payment method features
            if 'UPI' in narration:
                feature_dict['payment_upi'] = 1
                # Extract UPI app
                if '@PAYTM' in narration:
                    feature_dict['upi_paytm'] = 1
                elif '@OKAXIS' in narration or '@OKICICI' in narration:
                    feature_dict['upi_gpay'] = 1
                elif '@YBL' in narration:
                    feature_dict['upi_phonepe'] = 1
            elif 'ACH' in narration:
                feature_dict['payment_ach'] = 1
            elif 'ME DC' in narration:
                feature_dict['payment_card'] = 1
            
            # Merchant-specific features
            merchant = self._extract_merchant_name(row['Narration'])
            if merchant:
                # Check if merchant appears frequently
                feature_dict['merchant_frequency'] = sum(self.merchant_category_map.get(merchant, {}).values())
                
                # Most common category for this merchant
                if merchant in self.merchant_category_map:
                    most_common_cat = max(self.merchant_category_map[merchant].items(), 
                                        key=lambda x: x[1])[0]
                    feature_dict[f'merchant_usually_{most_common_cat}'] = 1
            
            # Amount patterns
            feature_dict.update({
                'is_round_amount': 1 if amount % 100 == 0 else 0,
                'is_small_amount': 1 if amount < 100 else 0,
                'is_large_amount': 1 if amount > 5000 else 0,
                'is_typical_bill': 1 if 1000 <= amount <= 10000 else 0,
                'is_typical_grocery': 1 if 50 <= amount <= 1000 else 0,
                'is_typical_food': 1 if 100 <= amount <= 500 else 0,
            })
            
            features.append(feature_dict)
        
        return pd.DataFrame(features).fillna(0)
    
    def _extract_merchant_name(self, narration):
        """Improved merchant extraction"""
        narration = str(narration).upper()
        
        # For UPI transactions
        if 'UPI-' in narration:
            parts = narration.split('-')
            if len(parts) >= 2:
                merchant = parts[1].strip()
                # Clean merchant name
                merchant = re.sub(r'[^A-Z0-9\s]', '', merchant)
                return merchant[:30]  # Limit length
        
        # For ACH transactions
        elif 'ACH' in narration:
            parts = narration.split('-')
            if len(parts) >= 2:
                return parts[1].strip()[:30]
        
        # For other transactions
        else:
            # Extract first meaningful part
            clean_narration = re.sub(r'[^A-Z0-9\s]', '', narration)
            return clean_narration[:30]
    
    def create_ensemble_model(self):
        """Create an ensemble of multiple models"""
        # Individual models
        rf = RandomForestClassifier(
            n_estimators=200,
            max_depth=15,
            min_samples_split=5,
            min_samples_leaf=2,
            random_state=42
        )
        
        gb = GradientBoostingClassifier(
            n_estimators=150,
            learning_rate=0.1,
            max_depth=5,
            random_state=42
        )
        
        lr = LogisticRegression(
            C=1.0,
            max_iter=1000,
            random_state=42
        )
        
        # Voting classifier
        ensemble = VotingClassifier(
            estimators=[
                ('rf', rf),
                ('gb', gb),
                ('lr', lr)
            ],
            voting='soft'
        )
        
        return ensemble
    
    def train(self, file_path):
        """Train the improved classifier"""
        print("Loading and preprocessing data...")
        df = self.load_and_preprocess_data(file_path)
        
        print("\nExtracting advanced features...")
        feature_df = self.extract_advanced_features(df)
        
        # Save feature columns for prediction consistency
        self.training_feature_columns = feature_df.columns.tolist()
        
        # Text features with better parameters
        self.vectorizer = TfidfVectorizer(
            max_features=200,
            ngram_range=(1, 3),  # Unigrams, bigrams, and trigrams
            min_df=1,
            stop_words='english',
            analyzer='word',
            token_pattern=r'\b[A-Za-z]+\b'  # Only alphabetic tokens
        )
        
        text_features = self.vectorizer.fit_transform(df['Narration'].fillna(''))
        
        # Combine features
        numeric_features = self.scaler.fit_transform(feature_df.values)
        X = np.hstack([text_features.toarray(), numeric_features])
        
        # Encode labels
        y = self.label_encoder.fit_transform(df['Category'])
        
        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=42, stratify=y
        )
        
        print("\nTraining ensemble model...")
        self.model = self.create_ensemble_model()
        
        # Hyperparameter tuning for Random Forest (fastest to tune)
        rf_model = RandomForestClassifier(random_state=42)
        param_grid = {
            'n_estimators': [100, 200],
            'max_depth': [10, 15, 20],
            'min_samples_split': [2, 5],
            'min_samples_leaf': [1, 2]
        }
        
        print("Performing hyperparameter tuning...")
        grid_search = GridSearchCV(
            rf_model, param_grid, cv=3, 
            scoring='accuracy', n_jobs=-1, verbose=1
        )
        grid_search.fit(X_train, y_train)
        
        print(f"Best parameters: {grid_search.best_params_}")
        print(f"Best CV score: {grid_search.best_score_:.3f}")
        
        # Train final model
        self.model.fit(X_train, y_train)
        
        # Evaluate
        y_pred = self.model.predict(X_test)
        accuracy = accuracy_score(y_test, y_pred)
        
        print(f"\nTest Accuracy: {accuracy:.2%}")
        
        # Detailed report
        print("\nClassification Report:")
        target_names = self.label_encoder.classes_
        labels = np.unique(np.concatenate([y_test, y_pred]))
        print(classification_report(y_test, y_pred, labels=labels, target_names=[target_names[i] for i in labels]))
        
        # Confusion matrix
        self._plot_confusion_matrix(y_test, y_pred, target_names)
        
        # Feature importance (from Random Forest component)
        if hasattr(self.model, 'estimators_'):
            rf_model = self.model.estimators_[0][1]  # Get RF from ensemble
            self._plot_feature_importance(rf_model, feature_df.columns)
        
        # Save the trained model
        self.save_model()
        
        return accuracy
    
    def _plot_confusion_matrix(self, y_true, y_pred, labels):
        """Plot confusion matrix"""
        cm = confusion_matrix(y_true, y_pred)
        plt.figure(figsize=(10, 8))
        sns.heatmap(cm, annot=True, fmt='d', cmap='Blues', 
                    xticklabels=labels, yticklabels=labels)
        plt.title('Confusion Matrix')
        plt.ylabel('True Label')
        plt.xlabel('Predicted Label')
        plt.tight_layout()
        plt.show()
    
    def _plot_feature_importance(self, model, feature_names):
        """Plot top feature importances"""
        # Get feature importances
        text_feature_names = [f'text_{i}' for i in range(len(self.vectorizer.get_feature_names_out()))]
        all_feature_names = text_feature_names + list(feature_names)
        
        importances = model.feature_importances_
        indices = np.argsort(importances)[::-1][:20]  # Top 20
        
        plt.figure(figsize=(10, 6))
        plt.title("Top 20 Feature Importances")
        plt.bar(range(20), importances[indices])
        plt.xticks(range(20), [all_feature_names[i] for i in indices], rotation=90)
        plt.tight_layout()
        plt.show()
    
    def predict(self, narration, amount, date=None):
        """Predict category for new transaction"""
        # Create a temporary DataFrame
        df = pd.DataFrame({
            'Narration': [narration],
            'Withdrawal Amt.': [amount],
            'Deposit Amt.': [0],
            'Date': [date if date else datetime.now()]
        })
        
        # Extract features - need to ensure same number of features as training
        feature_df = self.extract_advanced_features(df)
        
        # Ensure feature_df has same columns as training data
        if hasattr(self, 'training_feature_columns'):
            # Add missing columns with 0 values
            for col in self.training_feature_columns:
                if col not in feature_df.columns:
                    feature_df[col] = 0
            # Reorder columns to match training
            feature_df = feature_df[self.training_feature_columns]
        
        text_features = self.vectorizer.transform([narration])
        numeric_features = self.scaler.transform(feature_df.values)
        X = np.hstack([text_features.toarray(), numeric_features])
        
        # Predict
        y_pred = self.model.predict(X)[0]
        y_proba = self.model.predict_proba(X)[0]
        
        # Get category name
        category = self.label_encoder.inverse_transform([y_pred])[0]
        
        # Get top 3 predictions
        top_indices = np.argsort(y_proba)[::-1][:3]
        top_predictions = []
        for idx in top_indices:
            cat = self.label_encoder.inverse_transform([idx])[0]
            prob = y_proba[idx]
            top_predictions.append((cat, prob))
        
        return category, top_predictions
    
    def save_model(self, model_path='trained_model.pkl'):
        """Save the trained model and all required components"""
        model_data = {
            'model': self.model,
            'vectorizer': self.vectorizer,
            'scaler': self.scaler,
            'label_encoder': self.label_encoder,
            'merchant_category_map': self.merchant_category_map,
            'training_feature_columns': getattr(self, 'training_feature_columns', [])
        }
        
        with open(model_path, 'wb') as f:
            pickle.dump(model_data, f)
        print(f"Model saved to {model_path}")
    
    def load_model(self, model_path='trained_model.pkl'):
        """Load a pre-trained model"""
        if not os.path.exists(model_path):
            raise FileNotFoundError(f"Model file {model_path} not found")
        
        with open(model_path, 'rb') as f:
            model_data = pickle.load(f)
        
        self.model = model_data['model']
        self.vectorizer = model_data['vectorizer']
        self.scaler = model_data['scaler']
        self.label_encoder = model_data['label_encoder']
        self.merchant_category_map = model_data['merchant_category_map']
        self.training_feature_columns = model_data.get('training_feature_columns', [])
        
        print(f"Model loaded from {model_path}")
    
    def is_model_trained(self, model_path='trained_model.pkl'):
        """Check if a trained model exists"""
        return os.path.exists(model_path) and self.model is not None

# Usage example
if __name__ == "__main__":
    classifier = ImprovedTransactionClassifier()
    
    # Train the model
    accuracy = classifier.train('../files/expenses.csv')
    
    # Test predictions
    test_transactions = [
        ("UPI-ZOMATO LIMITED-ZOMATOORDER1.GPAY@OKPAYAXIS", 250),
        ("UPI-TANGEDCO-TANGEDCO@INDIANBK", 3500),
        ("ACH D- HDFC BANK LOAN-9000000000000", 15000),
        ("UPI-APOLLO PHARMACY-APOLLO@HDFCBANK", 450)
    ]
    
    print("\n\nTest Predictions:")
    print("-" * 80)
    
    for narration, amount in test_transactions:
        category, top_preds = classifier.predict(narration, amount)
        print(f"Transaction: {narration}")
        print(f"Amount: ₹{amount}")
        print(f"Predicted: {category}")
        print("Confidence:")
        for cat, prob in top_preds:
            print(f"  {cat}: {prob:.2%}")
        print("-" * 80)