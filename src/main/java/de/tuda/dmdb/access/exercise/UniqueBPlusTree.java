package de.tuda.dmdb.access.exercise;

import de.tuda.dmdb.access.AbstractTable;
import de.tuda.dmdb.access.UniqueBPlusTreeBase;
import de.tuda.dmdb.access.AbstractIndexElement;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;

/**
 * Unique B+-Tree implementation 
 * @author cbinnig
 *
 * @param <T>
 */
public class UniqueBPlusTree<T extends AbstractSQLValue> extends UniqueBPlusTreeBase<T> {
	
	/**
	 * Constructor of B+-Tree with user-defined fil-grade
	 * @param table Table to be indexed
	 * @param keyColumnNumber Number of unique column which should be indexed
	 * @param fillGrade fill grade of index
	 */
	public UniqueBPlusTree(AbstractTable table, int keyColumnNumber, int fillGrade) {
		super(table, keyColumnNumber, fillGrade);
	} 
	
	/**
	 * Constructor for B+-tree with default fill grade
	 * @param table table to be indexed 
	 * @param keyNumber Number of unique column which should be indexed
	 */
	public UniqueBPlusTree(AbstractTable table, int keyColumnNumber) {
		this(table, keyColumnNumber, DEFAULT_FILL_GRADE);
	}

	/**
	 * @param record record to be inserted
	 * @return true if insertion was correct, false otherwise
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean insert(AbstractRecord record) {
		//insert record
		//T key = (T) record.getValue(this.keyColumnNumber);
		
		//TODO: implement this method

		return true;
	}

	/**
	 * @param key key of the record to find
	 * @return if found, returns the record, otherwise null
	 */
	@Override
	public AbstractRecord lookup(T key) {
		//TODO: implement this method
		return null;
	}

}
