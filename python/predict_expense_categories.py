import pandas as pd
import sys
import time
from improved_categorizer import ImprovedTransactionClassifier

def predict_expense_categories(excel_file_path, model_path='trained_model.pkl', training_data_path='../files/expenses.csv', save_output=True, verbose=True):
    """
    Predict expense categories for transactions in an Excel file.
    
    Args:
        excel_file_path (str): Path to the Excel file containing transactions
        model_path (str): Path to the trained model file
        training_data_path (str): Path to training data CSV file (used if model doesn't exist)
        save_output (bool): Whether to save results to Excel file
        verbose (bool): Whether to print progress messages
    
    Returns:
        pandas.DataFrame: DataFrame with predicted categories
    """
    start_time = time.time()
    # Load the Excel file
    df = pd.read_excel(excel_file_path)
    if verbose:
        print("Loaded data shape:", df.shape)
        print("\nColumns:", df.columns.tolist())
        print("\nFirst few rows:")
        print(df.head())
    
    # Initialize classifier and check if model exists
    classifier = ImprovedTransactionClassifier()
    
    try:
        if verbose:
            print("\nLoading pre-trained model...")
        classifier.load_model(model_path)
    except FileNotFoundError:
        if verbose:
            print("\nNo pre-trained model found. Training classifier...")
        accuracy = classifier.train(training_data_path)
    
    # Predict categories for expenses
    if verbose:
        print("\nPredicting categories for expenses...")
    predictions = []
    
    for index, row in df.iterrows():
        narration = str(row.get('Narration', ''))
        withdrawal_amount = float(row.get('Withdrawal Amt.', 0) if pd.notna(row.get('Withdrawal Amt.', 0)) else 0)
        deposit_amount = float(row.get('Deposit Amt.', 0) if pd.notna(row.get('Deposit Amt.', 0)) else 0)
        ref_num = str(row.get('Chq./Ref.No.', ''))
        
        if withdrawal_amount > 0:  # Only predict for withdrawal transactions
            category, top_preds = classifier.predict(narration, withdrawal_amount)
            predictions.append({
                'Date': row.get('Date', ''),
                'Narration': narration,
                'Cheque / Reference no.': ref_num,
                'Withdrawal Amount': withdrawal_amount,
                'Deposit Amount': deposit_amount,
                'Category': category,
                'Cheque / Reference no.': ref_num
            })
    
    # Create results DataFrame
    results_df = pd.DataFrame(predictions)
    if verbose:
        print(f"\nPredicted categories for {len(results_df)} transactions:")
    
    # Save results if requested
    if save_output:
        output_file_path = excel_file_path.replace('.xlsx', '_categorized.xlsx')
        results_df.to_excel(output_file_path, index=False)
        if verbose:
            print(f"\nResults saved to {output_file_path}")
    
    # Show category distribution
    if verbose:
        print("\nCategory distribution:")
        print(results_df['Category'].value_counts())
    
    # Calculate and display total time taken
    end_time = time.time()
    total_time = end_time - start_time
    if verbose:
        print(f"\nTotal time taken: {total_time:.2f} seconds")
    
    return results_df

def main():
    """Main function for command line usage"""
    # Check if Excel file path is provided as command line argument
    if len(sys.argv) != 2:
        print("Usage: python predict_expense_categories.py <excel_file_path>")
        sys.exit(1)
    
    excel_file_path = sys.argv[1]
    predict_expense_categories(excel_file_path)

if __name__ == "__main__":
    main()