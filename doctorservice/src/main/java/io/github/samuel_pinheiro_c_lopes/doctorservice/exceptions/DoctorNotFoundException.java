package io.github.samuel_pinheiro_c_lopes.doctorservice.exceptions;
public class DoctorNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DoctorNotFoundException(final String message) {
		super(message);
	}
}