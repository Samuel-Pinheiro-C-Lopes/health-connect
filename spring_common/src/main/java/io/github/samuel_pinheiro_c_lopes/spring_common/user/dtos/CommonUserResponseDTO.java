package io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos;

public interface CommonUserResponseDTO {
	Long id();
	String name();
	String phone();
	String email();
	Long patientId();
	Long doctorId();
	String postalCode(); 
	String avenue(); 
	String complement(); 
	String number(); 
	String city(); 
	String district(); 
	String state();
}
