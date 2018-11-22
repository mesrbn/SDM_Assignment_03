package de.tuda.dmdb.storage.types.exercise;

import de.tuda.dmdb.storage.types.SQLIntegerBase;

/**
 * SQL integer value
 * @author cbinnig
 *
 */
public class SQLInteger extends SQLIntegerBase {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with default value
	 */
	public SQLInteger(){
		super();
	}
	
	/**
	 * Constructor with value
	 * @param value Integer value
	 */
	public SQLInteger(int value){
		super(value);
	}
	
	@Override
	public byte[] serialize() {
		//TODO: Insert your own implementation from exercise02
		byte[] bytes = new byte[4];

		for(int i = 0; i < 4; ++i) {
			bytes[i] = (byte)(this.value >>> i * 8);
		}

		return bytes;
	}

	@Override
	public void deserialize(byte[] data) {
		//TODO: Insert your own implementation from exercise02
		int length = data.length;
		if (data.length > 4) {
			System.out.println("WARN: Passed data array is to long, will only take the first 4 bytes!");
			length = 4;

		}
	}
	
	
	@Override
	public SQLInteger clone(){
		return new SQLInteger(this.value);
	}

}
