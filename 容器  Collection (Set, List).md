# -容器/Collection (Set无重复, List有序)

## Collection通用方法

1. size();
2. remove(); 移出容器
3. clear(); 清空（移除）
4. delete();删除对象
5. toArray();
6. contains(e);
7.  isEmpty(a);
8. a.addAll(Collection B)  ：B中的所有元素->A
9. a.removeAll(Collection C) : 移除A中与C容器相同的元素
10. a.retainAll(Collection C) : 取交集留在A
11. a.containsAll(Collection C) : 如果A中有C中所有元素，true

## 1.数组 

---

1. 访问快速，效率高，可重复-equals
2. 不灵活（容量要事先确定）

 

## 2.泛型 Collection<E~String> c = new ArrayList<>();

---

1. 数据类型的参数化
2. 数据类型的一个占位符
3. 编译器调用泛型必须传入实际类型

## ArrayList

> 查询效率高，增删效率低，线程不安全
>
> 实际上是把自己拷贝一份到被删除元素的index那里

> 数组扩容  实际上是把自己拷贝一份到自己这里

DEFAULT_CAPACITY =10  

扩容 ：new Object [ size + (size >>1) ]  System.arraycopy(elementData, 0, newArray,0,elementData.length)

## LinkedList

> 查询效率低，增删效率高

```java
Class Node{
	Node previous;
	Node next;
	Object element;
}
```

## Vector

> 线程安全

## Set

 元素不重复

HashSet/TreeSet实际上就是HashMap/TreeMap实现的

### TreeSet 按照元素递增排好了序

# Map

## HashMap                                                            （HashTable线程安全效率低，不允许k,v为null）

1. put(key,value);
2. get(k)
3. remove(k)
4. containsKey/Value
5. isEmpty
6. putAll(Map t)
7. clear

----

### JDK 8 后链表长于8会转化为红黑树

 

---

## TreeMap 排序才用 红黑二叉树

内部按照Key自增的方式排序

Implements comparable 自定义比较顺序

# 迭代器

Interface Iterator<E> 
建议：用于遍历时删除集合中的元素

hasNext(), next()

```java
for(Iterator<String> iter = list.iterator();iter.hasNext();){
	String temp = iter.next();
	sout(temp);
}
//遍历Map 1. 键值对
Set<Entry<Integer,String>> ss = Map1.entry.Set();

for(Iterator<Entry<Integer,String>> iter = ss.iterator();iter.hasNext();){
    Entry<Integer,String> trmp = iter.next();
    sout(temp);
}
// Map 2. 键集合
Set<Integer> keySet = map1.keySer();

for(Iterator<Integer> iter = keySet.iterator();iter.hasNext();){
	Integer key = iter.next();
    sout(map.get(key));
}

```

# Class Collections 工具类

shuffle、reverse、sort、binarySearch、

# JavaBean

需要有set,get,无参构造器