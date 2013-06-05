package aaEncoding;
/**
 * Interface für Encodings
 * Falls zur Laufzeit Encodings erstellt werden sollen, die NICHT im
 * Configurationsfile für AAIndizes gelistet werden sollen, müssen sie dieses
 * Interface implementieren.
 */
public interface IAAEncoding 
{
	public Double[] getEncodingValues ();
	public String getEncodingID();
	public String getDescription();
}
