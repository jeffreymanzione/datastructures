package com.jeffreymanzione.maps.heaps;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BinaryHeap<P extends Comparable<P>, E> extends AbstractHeap<P, E> {

	private List<BinaryEntry<P, E>> array = new ArrayList<>();

	private class BinaryEntry<K, V> extends Entry<K, V> {
		public BinaryEntry(K priority, V element) {
			super(priority, element);
		}

		@SuppressWarnings("unchecked")
		public BinaryEntry<K, V> parent() {
			return array.indexOf(this) == 0 ? null : (BinaryEntry<K, V>) array
					.get((array.indexOf(this) - 1) / 2);
		}

		@SuppressWarnings("unchecked")
		public BinaryEntry<K, V> left() {
			return (array.indexOf(this) * 2 + 1) > size() - 1 ? null
					: (BinaryEntry<K, V>) array
							.get(array.indexOf(this) * 2 + 1);
		}

		@SuppressWarnings("unchecked")
		public BinaryEntry<K, V> right() {
			return (array.indexOf(this) * 2 + 2) > size() - 1 ? null
					: (BinaryEntry<K, V>) array
							.get(array.indexOf(this) * 2 + 2);
		}
	}

	public Entry<P, E> getBottom() {
		return array.get(array.size() - 1);
	}

	public Entry<P, E> getTop() {
		return array.get(0);
	}

	@Override
	public E delete() {
		if (array.size() == 0) {
			throw new NoSuchElementException();
		} else {
			BinaryEntry<P, E> top = (BinaryEntry<P, E>) getTop();
			BinaryEntry<P, E> bottom = (BinaryEntry<P, E>) getBottom();

			E element = top.value;

			top.key = bottom.key;
			top.value = bottom.value;
			array.remove(bottom);

			if (top != bottom) {
				siftDown(top);
			}

			return element;
		}
	}

	private void swap(BinaryEntry<P, E> one, BinaryEntry<P, E> two) {
		P tempP = two.key;
		E tempE = two.value;
		two.key = one.key;
		two.value = one.value;

		one.key = tempP;
		one.value = tempE;
	}

	private void siftDown(BinaryEntry<P, E> top) {
		if (!((top.left() == null || top.key
				.compareTo(top.left().key) >= 0) && (top.right() == null || top.key
				.compareTo(top.right().key) >= 0))) {

			BinaryEntry<P, E> max;
			if (top.left() != null) {
				if (top.right() == null) {
					max = top.left();

				} else {
					max = top.left().key.compareTo(top.right().key) >= 0 ? top
							.left() : top.right();

				}

				swap(top, max);
				siftDown(max);
			}
		}
	}

	@Override
	public boolean delete(E element) {
		int index = array.indexOf(element);

		if (index < 0) {
			return false;
		} else {
			swap(array.get(index), array.get(array.size() - 1));
			array.remove(array.size() - 1);
			if (array.size() > 1) {
				siftDown(array.get(index));
			}

			return true;
		}
	}

	public Entry<P, E> deleteEntry() {
		if (array.size() == 0) {
			throw new NoSuchElementException();
		} else {
			BinaryEntry<P, E> top = (BinaryEntry<P, E>) getTop();
			BinaryEntry<P, E> bottom = (BinaryEntry<P, E>) getBottom();

			BinaryEntry<P, E> oldTop = top;

			array.set(0, bottom);
			array.remove(array.get(array.size() - 1));

			if (top != bottom) {
				siftDown(top);
			}

			return oldTop;
		}
	}

	@Override
	public int size() {
		return array.size();
	}

	@Override
	public void insert(P priority, E element) {
		BinaryEntry<P, E> node = new BinaryEntry<P, E>(priority, element);
		array.add(node);

		this.siftUp(node);

	}

	private void siftUp(BinaryEntry<P, E> node) {
		BinaryEntry<P, E> parent = node.parent();
		if (parent != null) {
			if (parent.key.compareTo(node.key) < 0) {
				P tempP = parent.key;
				E tempE = parent.value;
				parent.key = node.key;
				parent.value = node.value;

				node.key = tempP;
				node.value = tempE;

				siftUp(parent);
			}
		}
	}

	@Override
	public void insert(Entry<P, E> entry) {
		this.insert(entry.getKey(), entry.getValue());
	}

	@Override
	public boolean contains(E element) {
		return array.parallelStream().anyMatch(
				entry -> entry.getValue().equals(element));
	}

	@Override
	public void clear() {
		array.clear();
	}

}
