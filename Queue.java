//Victoria Severin
//1539768
//Queue.java
//Queue ADT class


public class Queue implements QueueInterface{

private class Node {
	Object item;
	Node next;

	Node(Object item){
	this.item = item;
	next = null;
	}
}

	//fields
	private Node front;
	private int size;

	//Constructor
	public Queue(){
	front = null;
	size = 0;
	}
 
	// isEmpty()
   	// pre: none
  	// post: returns true if this Queue is empty, false otherwise
   	public boolean isEmpty(){
	return (size==0);
	}

   	// length()
   	// pre: none
   	// post: returns the length of this Queue.
   	public int length(){
	return size;
	}

   	// enqueue()
   	// adds newItem to back of this Queue
   	// pre: none
   	// post: !isEmpty()
   	public void enqueue(Object newItem){
		if (front == null) {
     			front = new Node(newItem);
    		}else{
      			Node N = front;
      			while(N.next != null) {
        			N = N.next;
      			}
      		N.next = new Node(newItem);
   		}
   		size++; 
	}

  	// dequeue()
   	// deletes and returns item from front of this Queue
   	// pre: !isEmpty()
   	// post: this Queue will have one fewer element
   	public Object dequeue() throws QueueEmptyException{
		if (front == null) {
     			 throw new QueueEmptyException("cannot dequeue() on an empty Queue");
   		} else {
      			Node N = front;
     			front = N.next;
     			size--;
      			return N.item;
   		 }
	}

   	// peek()
   	// pre: !isEmpty()
  	// post: returns item at front of Queue
  	public Object peek() throws QueueEmptyException{
		if (front == null) {
     			throw new QueueEmptyException("cannot peek() on an empty Queue");
   		} else {
      			return front.item;
   		 }
	}

   	// dequeueAll()
   	// sets this Queue to the empty state
   	// pre: !isEmpty()
   	// post: isEmpty()
   	public void dequeueAll() throws QueueEmptyException{
		if (front == null) {
      			throw new QueueEmptyException("cannot dequeueAll() on an empty Queue");
    		} else {
      			front = null;
      			size = 0;
    		}
	}

   	// toString()
   	// overrides Object's toString() method
   	public String toString(){
		String s=""; 
    		Node N = front;
    		while(N != null){
     		s += N.item + " " ;
     		 N = N.next;
   	 }
   	 return s;	
	}
}
