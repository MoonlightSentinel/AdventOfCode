package main

import (
	"fmt"
	"os"
	"strconv"
)

func generateA(start, mul, max int, c chan int) {
	for ; max > 0; max-- {
		start = (start * mul) % 2147483647;
		c <- start;
	}
	close(c);
}

func solveA(startA, startB, max int) int {
	return solve( func(chanA, chanB chan int) {
		go generateA(startA, 16807, max, chanA);
		go generateA(startB, 48271, max, chanB);
	});
}

func generateB(start, mul, max , div int, c chan int) {
	for ; max > 0; max-- {
		for {
			start = (start * mul) % 2147483647;
			
			if (0 == (start % div)) {
				 break;
			}	
		}
		c <- start;
	}
	close(c);
}

func solveB(startA, startB, max int) int {
	return solve( func(chanA, chanB chan int) {
		go generateB(startA, 16807, max, 4, chanA);
		go generateB(startB, 48271, max, 8, chanB);
	});
}

func solve(fork func(chan int, chan int)) int {
	chanA := make(chan int, 100);
	chanB := make(chan int, 100);

	fork(chanA, chanB);

	count := 0;
	
	for a := range chanA {
		b := <- chanB;

		if(0 == ((a ^ b) & 0xFFFF)) {
			count++;
		}
	}
	return count;
}


func solveBoth(startA, startB int) (int, int){
	return 	solveA(startA, startB, 40000000),
		solveB(startA, startB, 5000000);
}

func main() {
	if(len(os.Args) == 3) {
		A, errA := strconv.Atoi(os.Args[1]);
		B, errB := strconv.Atoi(os.Args[2]);

		if (errA != nil || errB != nil) {
			fmt.Println("Invalid input");
		} else {
			fmt.Println(solveBoth(A, B));
		}
	} else {
		fmt.Println(solveBoth(873, 583));
	}
}
