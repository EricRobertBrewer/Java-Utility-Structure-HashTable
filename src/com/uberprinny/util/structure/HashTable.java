package com.uberprinny.util.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class HashTable<K, V> implements Map<K, V> {
	
	private class Entry<K2 extends K, V2 extends V> implements Map.Entry<K, V> {
		
		Entry(K2 key, V2 value) {
			mKey = key;
			mValue = value;
		}
		
		private K2 mKey;
		
		private V mValue;

		@Override
		public K getKey() {
			return mKey;
		}

		@Override
		public V getValue() {
			return mValue;
		}

		@Override
		public V setValue(V value) {
			V val = mValue;
			mValue = value;
			return val;
		}
	}
	
	public HashTable() {
		@SuppressWarnings("unchecked")
		LinkedList<Entry<K, V>>[] a = (LinkedList<Entry<K, V>>[]) new LinkedList[HASH];
		mTray = a;
	}
	
	private static final int HASH = 107; // prime
	
	private V mNull = null;
	
	private LinkedList<Entry<K, V>>[] mTray;
	
	private int mSize = 0;
	
	private LinkedList<Entry<K, V>> getTray(Object key) {
		int code = key.hashCode()%HASH;
		if (mTray[code] == null) {
			mTray[code] = new LinkedList<Entry<K, V>>();
		}
		return mTray[code];
	}
	
	@Override
	public void clear() {
		mNull = null;
		for (int i = 0; i < HASH; i++) {
			mTray[i] = null;
		}
		mSize = 0;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return mNull != null;
		}
		for (Entry<K, V> entry : getTray(key)) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if (mNull != null && mNull.equals(value)) {
			return true;
		}
		for (LinkedList<Entry<K, V>> tray : mTray) {
			if (tray != null) {
				for (Entry<K, V> entry : tray) {
					if (entry.getValue().equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		HashSet<Map.Entry<K, V>> s = new HashSet<Map.Entry<K, V>>();
		if (mNull != null) {
			s.add(new Entry<K, V>(null, mNull));
		}
		for (LinkedList<Entry<K, V>> tray : mTray) {
			if (tray != null) {
				for (Entry<K, V> entry : tray) {
					s.add(entry);
				}
			}
		}
		return s;
	}

	@Override
	public V get(Object key) {
		if (key == null) {
			return mNull;
		}
		for (Entry<K, V> entry : getTray(key)) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return mSize == 0 && mNull == null;
	}

	@Override
	public Set<K> keySet() {
		HashSet<K> s = new HashSet<K>();
		for (LinkedList<Entry<K, V>> tray : mTray) {
			if (tray != null) {
				for (Entry<K, V> entry : tray) {
					s.add(entry.getKey());
				}
			}
		}
		return s;
	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			V old = mNull;
			mNull = value;
			return old;
		}
		LinkedList<Entry<K, V>> tray = getTray(key);
		for (Entry<K, V> entry : tray) {
			if (entry.getKey().equals(key)) { // maintains uniqueness of keys
				return entry.setValue(value);
			}
		}
		tray.push(new Entry<K, V>(key, value));
		mSize++;
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (K key : m.keySet()) {
			put(key, m.get(key));
		}
	}

	@Override
	public V remove(Object key) {
		if (key == null) {
			V old = mNull;
			mNull = null;
			return old;
		}
		LinkedList<Entry<K, V>> tray = getTray(key);
		for (int i = 0; i < tray.size(); i++) {
			Entry<K, V> entry = tray.get(i);
			if (entry.getKey().equals(key)) {
				tray.remove(i);
				mSize--;
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public int size() {
		return mSize + (mNull != null ? 1 : 0);
	}

	@Override
	public Collection<V> values() {
		ArrayList<V> values = new ArrayList<V>();
		if (mNull != null) {
			values.add(mNull);
		}
		for (LinkedList<Entry<K, V>> tray : mTray) {
			if (tray != null) {
				for (Entry<K, V> entry : tray) {
					values.add(entry.getValue());
				}
			}
		}
		return values;
	}
}