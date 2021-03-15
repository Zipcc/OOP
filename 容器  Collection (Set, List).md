# -容器/Collection (Set无重复, List有序)

## Collection通用方法

1. size();
2. remove(); 移出容器
3. clear(); 清空（移除）
4. delete();删除对象
5. toArray();
6. contains(e);
7.  isEmpty(a);
8. a.addAll(Collection B)  ：B的所有元素->A
9. a.removeAll(Collection C) : 移除A中与C容器相同的元素
10. a.retainAll(Collection C) : 取交集留在A
11. a.containsAll(Collection C) : 如果A中有C中所有元素，true

## 1.数组 

---

1. 访问快速，效率高
2. 不灵活（容量要事先确定）



## 2.泛型 Collection<E~String> c = new ArrayList<>();

---

1. 数据类型的参数化
2. 数据类型的一个占位符
3. 编译器调用泛型必须传入实际类型