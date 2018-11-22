package de.tuda.dmdb.access.exercise;

import de.tuda.dmdb.access.AbstractIndexElement;
import de.tuda.dmdb.access.NodeBase;
import de.tuda.dmdb.access.UniqueBPlusTreeBase;
import de.tuda.dmdb.storage.AbstractRecord;
import de.tuda.dmdb.storage.types.AbstractSQLValue;
import de.tuda.dmdb.storage.types.exercise.SQLInteger;

/**
 * Index node
 * @author cbinnig
 *
 */
public class Node<T extends AbstractSQLValue> extends NodeBase<T>{

	/**
	 * Node constructor
	 * @param uniqueBPlusTree TODO
	 */
	public Node(UniqueBPlusTreeBase<T> uniqueBPlusTree){
		super(uniqueBPlusTree);
	}


	/**
	 * @param key key of the record to find
	 * @return if found, returns the record, otherwise null
	 */
	@Override
	public AbstractRecord lookup(T key) {
		int slotNumber = this.binarySearch(key);
		AbstractRecord nodeRecord = this.uniqueBPlusTree.getNodeRecPrototype().clone();
		this.indexPage.read(slotNumber, nodeRecord);
		SQLInteger pageNumber = (SQLInteger)nodeRecord.getValue(1);
		AbstractIndexElement<T> childNode = this.uniqueBPlusTree.getIndexElement(pageNumber.getValue());
		return childNode.lookup(key);

	}

	/**
	 * @param key key of the record
	 * @param record record to be inserted
	 * @return true if insertion was correct, false otherwise
	 */
	@Override
	public boolean insert(T key, AbstractRecord record){
		//TODO: implement this method
		int slotNumber = this.binarySearch(key);
		AbstractRecord nodeRecord = this.uniqueBPlusTree.getNodeRecPrototype().clone();
		this.indexPage.read(slotNumber, nodeRecord);
		SQLInteger pageNumber = (SQLInteger)nodeRecord.getValue(1);
		AbstractIndexElement<T> child = this.uniqueBPlusTree.getIndexElement(pageNumber.getValue());
		boolean inserted = child.insert(key, record);
		if (inserted && child.isFull()) {
			AbstractIndexElement<T> child1 = child.createInstance();
			AbstractIndexElement<T> child2 = child.createInstance();
			child.split(child1, child2);
			nodeRecord.setValue(0, child2.getMaxKey());
			nodeRecord.setValue(1, new SQLInteger(child2.getPageNumber()));
			this.indexPage.insert(slotNumber, nodeRecord, false);
			nodeRecord.setValue(0, child1.getMaxKey());
			nodeRecord.setValue(1, new SQLInteger(child1.getPageNumber()));
			this.indexPage.insert(slotNumber, nodeRecord, true);
		}

		return inserted;

	}
	
	@Override
	public AbstractIndexElement<T> createInstance() {
		return new Node<T>(this.uniqueBPlusTree);
	}
	
}