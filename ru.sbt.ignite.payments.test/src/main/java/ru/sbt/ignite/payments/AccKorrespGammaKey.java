package ru.sbt.ignite.payments;

public class AccKorrespGammaKey {
	public Integer KORRESP;
	public String ISO_DIG;
	public String CL_CODEWH;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CL_CODEWH == null) ? 0 : CL_CODEWH.hashCode());
		result = prime * result + ((ISO_DIG == null) ? 0 : ISO_DIG.hashCode());
		result = prime * result + ((KORRESP == null) ? 0 : KORRESP.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccKorrespGammaKey other = (AccKorrespGammaKey) obj;
		if (CL_CODEWH == null) {
			if (other.CL_CODEWH != null)
				return false;
		} else if (!CL_CODEWH.equals(other.CL_CODEWH))
			return false;
		if (ISO_DIG == null) {
			if (other.ISO_DIG != null)
				return false;
		} else if (!ISO_DIG.equals(other.ISO_DIG))
			return false;
		if (KORRESP == null) {
			if (other.KORRESP != null)
				return false;
		} else if (!KORRESP.equals(other.KORRESP))
			return false;
		return true;
	}
	
	
}
