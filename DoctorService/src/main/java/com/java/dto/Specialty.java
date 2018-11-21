package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Specialty {
	@Id
	private String specialtyName;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Specialty other = (Specialty) obj;
		if (specialtyName == null) {
			if (other.specialtyName != null)
				return false;
		} else if (!specialtyName.toLowerCase().equals(other.specialtyName.toLowerCase()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((specialtyName == null) ? 0 : specialtyName.toLowerCase().hashCode());
		return result;
	}

}
