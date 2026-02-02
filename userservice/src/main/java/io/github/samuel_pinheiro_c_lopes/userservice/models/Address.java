package io.github.samuel_pinheiro_c_lopes.userservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
	@Column(nullable = false)
	private String postalCode;
	@Column(nullable = false)
	private String avenue;
	@Column(nullable = true)
	private String complement;
	@Column(nullable = true)
	private String number;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String district;
	@Column(nullable = false)
	private String state;
	
	public Address() {
		super();
	}
	
	public Address(
			String postalCode, 
			String avenue, 
			String complement, 
			String number, 
			String city, 
			String district, 
			String state
	) {
		this();
		this.postalCode = postalCode;
		this.avenue = avenue;
		this.complement = complement;
		this.number = number;
		this.city = city;
		this.district = district;
		this.state = state;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getAvenue() {
		return avenue;
	}
	public void setAvenue(String avenue) {
		this.avenue = avenue;
	}
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
