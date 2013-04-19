Test implementations for: http://vanillajava.blogspot.com/2013/04/low-gc-coding-efficient-listeners.html

Attemping to make a thread safe, zero GC observer that gives each observer a chance at being "first" in the iteration order for fairness.


note: the stat collections to verify 
Run from my iMac OSX 10.8.3, 3.4ghz i7, 16GB ram


    10000x100 Iterations used 8,008 bytes, last 10k 571,000 ns
      ArraySynchronized{sz=10, idx=1000000, first=[100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000], total=[1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000]}

    10000x100 Iterations used 0 bytes, last 10k 605,000 ns
	    CasCalls{sz=10, idx=1000000, first=[100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000], total=[1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000]}

    10000x100 Iterations used 1,840 bytes, last 10k 687,000 ns
	     ArrayLocked{sz=10, idx=1000000, first=[100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000], total=[1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000]}

