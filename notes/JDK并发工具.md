# 1. 同步控制

## 1.1 重入锁

重入锁可以完全替代synchronized关键字。在JDK5.0前，重入锁的性能是好于synchronized关键字的，但是自JDK6.0后，JDK对synchronized做了大量优化，所以现在二者性能差距不大。

1. 重入锁相较于synchronized，有着显式的操作过程，需要手动操作何时加锁，何时释放锁，所以更加灵活。但要记住手动释放锁，不然其他线程就不能访问临界区了。
2. 重入锁是之所以叫重入，是因为这种锁是可以反复进入的。 当一个线程多次获得锁的时候，在释放锁的时候就必须释放相同次数的锁。如果释放次数过多，会导致异常，释放次数过少，线程仍然持有锁，其他线程无法进入临界区。 
```Java
    lock.lock()；
    lock.lock()；
    lock.lock()；
    lock.unlock()；
    lock.unlock()；
    lock.unlock()；
```   
3. 重入锁可以处理中断响应，中断后，线程会退出。采用重入锁，可以有效地避免死锁问题。
4. 限时等待也可以避免死锁，使用tryLock()进行一次限时的等待。如果有参数，即为等待时长和计时单位。如果括号内没有参数，表示申请锁成功就会返回true，反之就会立即返回false。
5. 公平锁：在大部分情况下，锁都是不公平的。系统会从锁的等待队列中随机选择一个。而公平锁就是按照线程到达等待序列的前后顺序，因此公平锁不会产生饥饿现象。由于公平锁需要系统维护一个有序的队列，因此实现成本较高且性能相对低下。
6. condition条件：condition与重入锁想关联，通过重入锁的Lock接口中的newCondition()方法就可以生成一个Condition实例。通过Condition实例可以让线程在合适的时间进行等待或者继续执行。

```java
condition配合重入锁使用，方法为condition.await()和condition.signal().

Object中的wait()和notify()配合synchronized使用。

上述方法执行时均需要获得锁。
```

## 1.2 读写锁

对于读操作来说，并不会对数据的完整性造成破坏，因此所有读之间也需要等待锁，这种设计是不合理的。因此，读写分离锁(ReadWriteLock)可以有效的减少锁竞争。在系统中，读操作要远远大于写操作，则读写锁可以发挥最大的功效，提升系统的性能。

## 1.3 倒计时器和循环栅栏

- CountDownLatch（倒计时器）
    用来作为多线程控制工具类，使某一个线程直到倒计时结束，再开始执行。countDownLatch.cutDown()方法为计数器减一。而countDownLatch.await()方法可以让主线程等待倒计时器里的线程全部完成后才继续执行。
- CyclicBarrier(循环栅栏)
    也是用来作为多线程并发控制工具，它的功能与CountDownLatch相似，但更加强大。CyclicBarrier除了可以接受parties参数，作为计数器次数，还可以接受barrierAction（当计数器一次计数完成后，系统会执行的动作。）
## 1.4 线程阻塞类工具LockSupport

**LockSupport是一个线程阻塞工具，他可以在线程内的任意位置对线程进行阻塞。**


其静态方法park()可以阻塞线程，其原理是LockSupport给每一个线程准备了一个许可，如果许可可用，则park()会立即返回，并且将许可变为不可用。当许可为不可用时，线程就阻塞了。同时，还有parkNanos()和parkUntil()等，在许可可用前禁用当前线程，并最多等待指定的等待时间。除了有定时阻塞的功能，LockSupport.park( )还能支持中断影响，但是不会抛出InterruptedException异常。而unpark()则是将许可变为可用，但是这个许可不可叠加，永远只有一个，因此如果unpark()出现在park()之前，也可以使park()顺利结束。
```
对比Thread.suspend():他弥补了由于resume发生在前，线程无法继续执行的情况。同时，处于park()状态的线程会在jstack中明确给出一个WAITING状态，并标明阻塞对象。
对比Object.wait():它不需要先获得某个对象的锁，也不会抛出InterruptedException异常。
```

## 1.5 信号量(semaphore)
一般默认一个线程一次访问一个资源。通过信号量可以指定多个线程同时访问某一个资源。其主要方法有：acquire()和tryAcquire()以及release()方法。acquire方法尝试获得一个准入许可，如果无法获得就会一直等待。release()方法可以在线程访问完资源后释放许可，使得其他线程可以进行资源访问。

# 2. 线程池
- 出现原因：线程虽然是轻量级工具，但是开启和关闭仍要占用内存资源和时间，如果每个任务都进行线程的创建和销毁，很有可能会导致大量时间花费在线程的创建和销毁上，吞吐量反而下降。其次，过多的线程会增加内存溢出的风险，也会给GC带来压力。因此，生产环境中采用线程池来控制和管理线程，是必要的。

- 线程池：为了让创建的线程复用。当需要使用线程时，可以从池子中选择一个空闲线程，当完成工作后，再将这个线程返回线程池，方便别的线程使用。

- JDK中线程池有关API：
    ```java
    ExecutorService exec = Executors.newFixedThreadPool(10);
    ```
    Executors类扮演着线程池工厂的角色，通过Executors可以获得一个具有特定功能的线程池。可以返回的线程池包括：
        
    - newFixedThreadPool(int nThreads):返回一个固定线程数量的线程池。当有新任务提交时，若线程池中有空闲线程，则立即执行；若没有，任务被放在任务队列，等待线程空闲后，便处理队列中的任务。

    - newCachedThreadPool():返回一个根据实际情况调整线程数量的线程池。若有空闲线程复用，优先使用可以复用的线程。若所有线程都在工作，又有新任务提交，则会创建新的线程处理任务。所有线程完成后都会进入线程池。

    - newSingleThreadExecutor():返回只有一个线程的线程池。若有多余任务提交，则任务会被保存在一个任务队列中，**待线程空闲时，按照先入先出的顺序执行队列中的任务。**

    - newSingleThreadScheduledExecutor():返回一个ScheduledExecutorService对象，线程池的大小为1。ScheduledExecutorService接口定义如下：ScheduledExecutorService(Runnable,delay,unit)。表示在某个固定的延时之后执行，或者周期性地执行某个任务。

    - newScheduledThreadPool():该方法也返回一个ScheduledExecutorService对象，但是该线程可以指定线程的数量。
    
        *注意：在ScheduledExecutorService接口中，主要有两种方法的区别：scheduleAtFixedRate( )和scheduleWithFixedDelay( )。对于FixedRate方法来说，它是以上一个任务开始执行的时间作为起点，之后的period时间调度下一次任务；而FixedDelay()则是在上一个任务结束后，在经过delay的时间进行调度。*

- 线程池的核心内部实现：

    对于核心的几个线程池，其内部实现均采用了ThreadPoolExecutor实现。
    ```java
    public ThreadPoolExecutor(int corePoolSize,
                              int maxinumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
    ```
    其中，workQueue指被提交但是还没有执行的任务队列。按照队列功能分类，可以使用以下几种BlockingQueue。

    -  直接提交队列：有SynchronousQueue提供，其没有容量，每一个插入操作都要等待一个相对应的删除操作。如果新任务交给线程执行，没有空闲的进程的话，且进程数量已经达到了最大值，就会执行拒绝策略。
    - 有界的任务队列：有界的任务队列可以适用ArrayBlockingQueue实现。当使用有界的任务队列时，如果线程的corepoolsize未满，则优先创建新的线程；如果大于corepoolsize，则先将任务加入等待队列，等待队列满了的情况下，如果线程数不大于maxinumpoolsize，则创建新的线程，反之拒绝。
    - 无界的任务队列：无界任务队列使用LinkedBlockingQueue实现，除非系统资源耗尽，否则无界的任务队列不存在任务入队失败的情况。
    - 优先任务队列：通过PriorityBlockingQueue实现，可以控制任务的先后执行顺序，他是一个特殊的无界序列。可以根据任务的优先级顺序来先后执行。

    此外，RejectedExecutionHandler的拒绝策略如下：
    
    - AbortPolicy策略：直接抛出异常，阻止系统正常工作；
    - CallerRunPolicy策略：只要线程池未关闭，该策略就直接在调用者线程中运行被丢弃的任务。但是任务提交线程的性能会急速下降。
    - DiscardOledestPolicy策略： 丢弃最老的一个请求，并尝试再次提交当前任务。
    - DiscardPolicy策略：丢弃无法处理的任务。
- 线程池的注意点：线程池可能会淹没掉程序抛出的异常，导致无法发现程序的错误。常见的解决方法可以采用execute()替换submit()；或者使用submit()返回的Future对象使用get()方法，获得异常。当然也可以通过拓展ThreadPoolExecutor方法来处理。


# 3. JDK并发容器
