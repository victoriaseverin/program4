//Victoria Severin
//1539768
//QueueTest.java
//February 23, 2018
//Additional test client for Queue ADT for independent testing of its operations

public class QueueTest{
	public static void main (String[] args) {
	Queue A = new Queue();

	System.out.println(A.isEmpty());

	//the inserts
	A.enqueue((int)4);
	A.enqueue((int)2);
	A.enqueue((int)6);
	A.enqueue((int)8);

	//size
	//or if empty
	System.out.println(A.isEmpty());
	System.out.println(A.length());
	System.out.println(A);

	//delete
	A.dequeue();
	A.dequeue();

	System.out.println(A.isEmpty());
	System.out.println(A.length());
	System.out.println(A.peek());

	A.dequeueAll();
	System.out.println(A.isEmpty());
	System.out.println(A.length());
	
	try{
		System.out.println(A.peek());
	}catch(QueueEmptyException e){
		System.out.println(e);
		System.out.println("Continuing without interruption");
	}
	}
}
