# scala-packed

A \[early prototype of\] scala library for making memory-optimized data structures.

General idea:

* pack all members of case-class like objects to a single ByteArray buffer
* use a single shared buffer for sequences
* zero allocations while doing seq.map() as we only update buffer offset in iterator object
* sequences are immutable, but Trie-like, with O(1) append/prepend

### hello world example

    @Packed class HelloPacked(i: Int, l: Long)
    
    // apply method
    val hello = HelloPacked(1, 2L)
    
    // getters
    val i = hello.i
    val l = hello.l
    
    // list
    val seq = PackedSeq[HelloPacked]((0 to 1000).map(a => HelloPacked(a, a)))
    
    // iterating
    var sum = 0
    seq.foreach( item => sum += item.i )
    
    // mapping
    val twice = seq.map(a => HelloPacked(a.i * 2, a.l * 2))
    