package io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos;

public interface CommonPersonResponseDTO {
	Long id();
	String name();
	String phone();
	Long userId();
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
