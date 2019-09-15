package test;

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
	}
}

class Solution {
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode node1 =l1;
		ListNode node2 =l2;
		ListNode resultList;
		do{
			ListNode result = new ListNode(node1.val + node2.val);

			node1 = node1.next;
			node2 = node2.next;
		}while(node1 != null && node2 != null);
		
		return node1;
	       
	}
}

public class ProblemSolver {

	

	public static void main(String[] args) {
		ListNode l1 = new ListNode(2);
		l1.next = new ListNode(4);
		l1.next.next = new ListNode(3);
		
		ListNode l2 = new ListNode(5);
		l1.next = new ListNode(6);
		l1.next.next = new ListNode(4);
		
		Solution solution = new Solution();
		ListNode l3 = solution.addTwoNumbers(l1, l2);
		printList(l3);

	}

	private static void printList(ListNode l) {
		ListNode node = l;
		do{
			System.out.println(node.val);
			node = node.next;
		}while(node != null);
		
	}

}
