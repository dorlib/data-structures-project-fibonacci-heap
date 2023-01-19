# data-stractures-project-fibonacci-heap

This is the second programming project in data structures course. </br>
In this project we will implement a fibonacci heap. </br>

- [Heap Node class](#Heap-Node-Class)
    - [functions & time complexity table](#Heap-Node-functions-&-time-complexity)
- [Fibonacci Heap class](#Fibonacci-Heap-Class)
    - [functions & time complexity table](#Fibonacci-Heap-functions-&-time-complexity)
- [Contributing](#Contributing)
- [Maintainers](#Creators-/-Maintainers)

## Heap Node Class

Heap Node class implement a Node in the fibonacci heap. 

### Heap Node functions & time complexity

| Function     | Description                                         | Time Complexity | 
|:-------------|:----------------------------------------------------|:----------------|
| getKey()     | Returns the key field of this.                      | O(1)            |
| getRank()    | Returns the rank field of this.                     | O(1)            |
| isMarked()   | Returns true if this is marked, or false otherwise. | O(1)            |
| mark()       | Mark this.                                          | O(1)            |
| unMark()     | Unmark this.                                        | O(1)            |
| getChild()   | Returns the child field of this.                    | O(1)            |
| getParent()  | Returns the parent field of this.                   | O(1)            |
| getNext()    | Returns the next field of this.                     | O(1)            | 
| getPrev()    | Returns the prev field of this.                     | O(1)            |

## Fibonacci Heap Class

Fibonacci Heap include the implementation of the Heap's functionality and the methods that the user will use.
    
### Fibonacci Heap functions & time complexity

| Function                       | Description                                                                                             | Time Complexity | 
|:-------------------------------|:--------------------------------------------------------------------------------------------------------|:----------------|
| iEmpty()                       | Returns true if and only if the Heap is empty.                                                          | O(1)            |
| insert(int i)                  | Inserts to the heap new heap node with the key i and returns the new heap node.                         | O(1)            |
| deleteMin()                    | Removes the node with the minimal key from the heap.                                                    | O(log(n))       |
| findMin()                      | Returns the node with the minimal key from the heap.                                                    | O(log(n))       |
| meld(FibonacciHeap heap2)      | Melds heap2 with the current heap.                                                                      | O(1)            |
| size()                         | Returns the size of the heap.                                                                           | O(1)            |
| counterRep()                   | Returns an array where the i'th element represents the num of trees in the heap which their rank is i.  | O(n)            |
| delete(HeapNode x)             | Deletes x from the heap.                                                                                |                 | 
| decreaseKey(HeapNode x, int d) | Decreases the key of x by d.                                                                            | O(log(n))       |
| nonMarked()                    | Returns the number of nodes in the heap which are not marked.                                           | O(1)            |
| potential(lst)                 | Returns the current potential of the heap which we calculate by num of trees + 2 * num of marked nodes. | O(1)            |
| totalLinks()                   | Static function which returns the sum of links done while the app is running.                           | O(1)            |
| totalCuts()                    | Static function which returns the sum of cuts done while the app is running.                            | O(1)            |
| kMin(FibonacciHeap H, int k)   | Static function which returns a sorted array of the k smallest nodes in a heap.                         | O(k * deg(H))   |

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. Please make sure to update tests as appropriate.

### How To Contribute

1. Fork the repository to your own Github account.
2. Clone the project to your machine.
3. Create a branch locally with a succinct but descriptive name.
4. Commit changes to the branch.
5. Following any formatting and testing guidelines specific to this repo.
6. Push changes to your fork.
7. Open a Pull Request in my repository.

## Creators / Maintainers

- Dor Liberman ([dorlib](https://github.com/dorlib))
- Afik Ben Shimol ([AfikBenShimol](https://github.com/AfikBenShimol))

If you have any questions or feedback, I would be glad if you will contact me via mail.

<p align="left">
  <a href="dorlibrm@gmail.com"> 
    <img alt="Connect via Email" src="https://img.shields.io/badge/Gmail-c14438?style=flat&logo=Gmail&logoColor=white" />
  </a>
</p>

This project was created for educational purposes, for personal and open-source use.

If you like my content or find my code useful, give it a :star:
