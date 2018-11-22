package de.tuda.dmdb.storage.exercise;

import de.tuda.dmdb.storage.AbstractPage;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.Record;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.EnumSQLType;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;
import de.tuda.dmdb.storage.types.exercise.SQLVarchar;

import java.util.ArrayList;


public class RowPage extends AbstractPage {

	/**
	 * Constructor for a row page with a given (fixed) slot size
	 * @param slotSize
	 */
	public RowPage(int slotSize) {
		super(slotSize);
	}
	
	@Override
	public void insert(int slotNumber, AbstractRecord record, boolean doInsert) {
		//TODO: Insert your own implementation from exercise02
		boolean canFit = this.recordFitsIntoPage(record);
		if (canFit) {
			int startOffset = slotNumber * 12;
			ArrayList<byte[]> arrayList = insertHelper(record);
			if (!doInsert) {

				AbstractRecord temp = new Record(2);
				temp.setValue(0, new SQLInteger());
				temp.setValue(1, new SQLVarchar(10));
				read(slotNumber, temp);

				int a = record.getVariableLength();
				int b = temp.getVariableLength();


				if (a >= b) {
					System.arraycopy(arrayList.get(0), offset, this.data, slotNumber*12, arrayList.get(0).length);
					System.arraycopy(arrayList.get(1), offset, this.data, this.offsetEnd, arrayList.get(1).length);
				}
				else {
					throw new RuntimeException("There is not enough space!");
				}

			} else {
				int numRecToShift = numRecords - slotNumber;
				AbstractRecord insertRec = record;
				int place = slotNumber;
				int count = numRecords;
				AbstractRecord shiftyRec = new Record(2);
				for (int i = --count; i >= slotNumber; i--) {
					shiftyRec.setValue(0, new SQLInteger());
					shiftyRec.setValue(1, new SQLVarchar(10));
					this.read(i, shiftyRec);
					offsetEnd -= shiftyRec.getVariableLength();
					this.offsetEnd -= shiftyRec.getVariableLength();
					this.insert(i+1, shiftyRec, false);
				}
				//this.insert(place, insertRec, true);

				System.arraycopy(arrayList.get(0), 0, this.data, slotNumber*shiftyRec.getFixedLength(), arrayList.get(0).length);
				System.arraycopy(arrayList.get(1), 0, this.data, this.offsetEnd, arrayList.get(1).length);
				this.offsetEnd -= record.getVariableLength();

			}

		}else
			throw new RuntimeException("There is not enough space");
	}
	
	@Override
	public int insert(AbstractRecord record){
		boolean canFit = this.recordFitsIntoPage(record);

		if (canFit) {
			ArrayList<byte[]> arrayList = insertHelper(record);

			System.arraycopy(arrayList.get(0), 0, this.data, this.numRecords*12, arrayList.get(0).length);
			System.arraycopy(arrayList.get(1), 0, this.data, this.offsetEnd, arrayList.get(1).length);

			this.offsetEnd -= arrayList.get(1).length;
			this.numRecords++;
		} else
			throw new RuntimeException("There is not enough space");


		return numRecords - 1;

	}

	
	@Override
	public void read(int slotNumber, AbstractRecord record){
		//TODO: Insert your own implementation from exercise02
		SQLInteger sqlInteger;
		SQLVarchar sqlVarchar;
		int numberOfAttributes = record.getValues().length;
		for (int i = 0; i < numberOfAttributes; i++) {
			if (record.getValues()[i].getType() == EnumSQLType.SqlInteger) {
				sqlInteger = (SQLInteger) record.getValues()[i];
				byte[] elementInt = new byte[4];
				System.arraycopy(this.data, (slotNumber * 12) + (i*8), elementInt, 0, 4);
				sqlInteger.deserialize(elementInt);
			}
			// else it is varchar
			else {
				sqlVarchar = (SQLVarchar) record.getValues()[i];
				byte[] metaPart1 = new byte[4];
				System.arraycopy(this.data, (slotNumber * 12) + (i*4), metaPart1, 0, 4);
				byte[] metaPart2 = new byte[4];
				System.arraycopy(this.data, (slotNumber * 12) + (i*4) + 4, metaPart2, 0, 4);

				// calculate the length of the varchar
				sqlInteger = new SQLInteger();
				sqlInteger.deserialize(metaPart2);
				int varCharSize = sqlInteger.getValue();
				// calculate the offset of the varchar in page
				sqlInteger.deserialize(metaPart1);
				int varCharBeginAddress = sqlInteger.getValue();

				byte[] elementVarChar = new byte[varCharSize];
				System.arraycopy(this.data, varCharBeginAddress, elementVarChar, 0, varCharSize);
				sqlVarchar.deserialize(elementVarChar);
				SQLVarchar sqlVarchar1 = (SQLVarchar) record.getValues()[i];
				sqlVarchar1.deserialize(elementVarChar);
			}
		}


	}

	private ArrayList<byte[]> insertHelper(AbstractRecord record) {
		ArrayList<byte[]> arrayList = new ArrayList<>();

		int numberOfAttributes = record.getValues().length;
		int index = 0;

		byte[] recInt = null;
		byte[] recVarChar = null;
		byte[] recFixSize = new byte[12];
		for (int i = 0; i < numberOfAttributes; i++) {
			if (record.getValues()[i].getType() == EnumSQLType.SqlInteger) {
				recInt = record.getValues()[i].serialize();
				System.arraycopy(recInt, 0, recFixSize, index, recInt.length);
				index += recInt.length;
			}
			else if (record.getValues()[i].getType() == EnumSQLType.SqlVarchar) {
				recVarChar = record.getValues()[i].serialize();
				byte[] metaPart1 = new SQLInteger(this.offsetEnd).serialize();
				System.arraycopy(metaPart1, 0, recFixSize, index, metaPart1.length);
				index += metaPart1.length;
				byte[] metaPart2 = new SQLInteger(recVarChar.length).serialize();
				System.arraycopy(metaPart2, 0, recFixSize, index, metaPart2.length);
				index += metaPart2.length;
			}
			else
				throw new RuntimeException("not suitable data type");
		}
		arrayList.add(recFixSize);
		arrayList.add(recVarChar);


		return arrayList;
	}

}
