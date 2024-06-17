package com.um.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registration_token")
public class RegistrationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private long tokenId;

	@Column(name = "token")
	private String token;
	
	@Column(name = "authentication_id")
	private String authenticationId;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type")
	private AuthenticationType authenticationType;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "token_status")
	private TokenStatus tokenStatus;
	
	@Column(name = "event_timestamp")
	private LocalDateTime eventTimeStamp;
	
	@Column(name = "expiry_timestamp")
	private LocalDateTime expiryTimeStamp;

	public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	public TokenStatus getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(TokenStatus tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public LocalDateTime getEventTimeStamp() {
		return eventTimeStamp;
	}

	public void setEventTimeStamp(LocalDateTime eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}

	public LocalDateTime getExpiryTimeStamp() {
		return expiryTimeStamp;
	}

	public void setExpiryTimeStamp(LocalDateTime expiryTimeStamp) {
		this.expiryTimeStamp = expiryTimeStamp;
	}
	
}

