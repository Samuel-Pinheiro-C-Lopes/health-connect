package io.github.samuel_pinheiro_c_lopes.userservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String phone;
	@OneToOne(optional = false)
	private User user;
	@Column(name = "doctor_id", unique = true, nullable = true)
    private Long doctorId;
	@Column(name = "patient_id", unique = true, nullable = true)
    private Long patientId;
	@Embedded
	private Address address;
	
	public Person() {
		super();
	}

	public Person(
			String name,
			String phone,
			String city,
			String district,
			String state,
			String postalCode,
			String avenue,
			String number,
			String complement
	) {
		this();
		this.name = name;
		this.phone = phone;
		this.address = new Address(
				postalCode, 
				avenue, 
				complement, 
				number, 
				city, 
				district, 
				state
		);
	}
	
	public Person(
			String name,
			Long userId,
			String phone,
			String city,
			String district,
			String state,
			String postalCode,
			String avenue,
			String number,
			String complement
	) {
		this(name, phone, city, district, state, postalCode, avenue, number, complement);
		this.user = new User(userId);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
