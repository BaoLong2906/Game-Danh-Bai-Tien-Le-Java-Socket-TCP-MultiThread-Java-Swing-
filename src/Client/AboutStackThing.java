package Client;

import java.util.Scanner;
import java.util.Stack;

public class AboutStackThing {
	
	static Stack<String> stack = new Stack<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		stack.push("a");
		stack.push("b");
		stack.push("c");
		stack.push("d");
		
		while(true) {
			int cmd = new Scanner(System.in).nextInt();
			switch(cmd) {
			case 1:
				// back
				
			case 2:
				// forward
				
			case 3:
				// push
				stack.push("z");
			}
		}
	}

}
