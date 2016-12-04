package de.wps.dot.reservierung;

import static org.valid4j.Assertive.require;

public class Sitznummer {
	private final int sitzreihe;
	private final int stuhlnummer;

	private Sitznummer(int sitzreihe, int stuhlnummer) {
		this.sitzreihe = sitzreihe;
		this.stuhlnummer = stuhlnummer;
	}
	
	public static Sitznummer von(int sitzreihe, int stuhlnummer) {
		return new Sitznummer(sitzreihe, stuhlnummer);
	}
	
	public int gibSitzReihe() {
		return sitzreihe;
	}

	public static Sitznummer von(String sitzreihe, int stuhlnummer) {
		char c = sitzreihe.charAt(0);
		int i = (int)c - (int)('A');
		return new Sitznummer(i, stuhlnummer);
	}

	public static Sitznummer von(String sitznummer) {
		require(isValid(sitznummer));
		
		return von(sitznummer.substring(0,1), Integer.parseInt(sitznummer.substring(1,1)));
	}

	public String gibName() {
		String result = "" + (char)('A' + sitzreihe) + (stuhlnummer);

		return result;
	}

	public static boolean isValid(String sitznummer){
		if(sitznummer.substring(0,1).charAt(0) < 'A' || sitznummer.substring(0,1).charAt(0) > 'Z')
		{
			return false;
		}
		
		try {
			Integer.parseInt(sitznummer.substring(1, sitznummer.length()));
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sitzreihe;
		result = prime * result + stuhlnummer;
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
		Sitznummer other = (Sitznummer) obj;
		if (sitzreihe != other.sitzreihe)
			return false;
		if (stuhlnummer != other.stuhlnummer)
			return false;
		return true;
	}
}
