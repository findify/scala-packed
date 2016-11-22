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
    
### Performance

RAM usage is 5x-10x lower compared to generic collections of case classes.

CPU usage depends on workload, but in general is the same as scala native collections (which are quite slow BTW).

### TODO

Features not yet implemented:

* support for Float, Double, Byte types
* support for arbitrary types (like joda-time)
* support for nested packed classes

## Licence

The MIT License (MIT)

Copyright (c) 2016 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.