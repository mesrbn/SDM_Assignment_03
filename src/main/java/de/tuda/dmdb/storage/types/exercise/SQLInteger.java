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
		return null;
	}

	@Override
	public void deserialize(byte[] data) {
		//TODO: Insert your own implementation from exercise02
	}
	
	
	@Override
	public SQLInteger clone(){
		return new SQLInteger(this.value);
	}

}
