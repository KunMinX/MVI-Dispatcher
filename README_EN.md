**Development story**:

[《Android: Solving the Pain Points of MVI Architecture in Practice》](https://blog.devgenius.io/android-solving-the-pain-points-of-mvi-architecture-in-practice-4971fa9ed9c0)

&nbsp;

Reactive programming is conducive to unit testing, but it has its own flaws. MVI is designed to eliminate these flaws.

MVI has a certain threshold and is more cumbersome to implement. It also has performance issues, which may cause some colleagues to give up and return to traditional methods.

Overall, MVI is suitable for implementing a "modern development model" in combination with Jetpack Compose.

On the other hand, if you are pursuing "low cost, reusability, and stability", the problem can be solved from the source by following the "single responsibility principle".

In response to this, MVI-Dispatcher was born.

&nbsp;

|                      Collect or topped                       |                      Smooth transition                       |                         Delete notes                         |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://images.xiaozhuanlan.com/photo/2022/3555d17b46e04054154916d00f1214f8.gif) | ![](https://images.xiaozhuanlan.com/photo/2022/d20a18e90cda8aa1f7d6977dca7b7135.gif) | ![](https://images.xiaozhuanlan.com/photo/2022/5786c16f17612661b0b490dd40e78608.gif) |

&nbsp;

# Project Description

The author has long focused on the "business architecture" pattern and is committed to eliminating unexpected issues in the agile development process.

In this case, I will show you how MVI-Dispatcher can easily accomplish the previously complicated and error-prone message distribution process with just a few lines of code.

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch:7.6.0'
```

&nbsp;

A complete "domain layer" message distribution component should at least meet the following requirements:

1. It contains a message queue that can store messages that have been sent but not consumed.
2. When the page is not visible, any messages sent during the queue storage period will be automatically consumed when the page becomes visible again.

MVI-Dispatcher was born to meet these needs.

&nbsp;

Furthermore, the improvements and optimizations of MVI-Dispatcher include:

> 1. It can completely eliminate mutable boilerplate code, without writing a single line.
> 2. It can prevent new team members from misusing mutable.setValue() in Activity/Fragment.
> 3. Developers only need to focus on input and output. They inject events through the unique input entry point and observe them through the unique output exit point.
> 4. New team members can automatically implement "reactive" development based on the concise and easy-to-understand input-output design of MVI-Dispatcher without being familiar with LiveData, UnPeekLiveData, SharedFlow, mutable, or MVI.
> 5. It can be seamlessly integrated into Jetpack MVVM and other pattern projects.

&nbsp;

![](https://s2.loli.net/2023/05/18/JXHyColB2Knxmkq.jpg)

&nbsp;

MVI-Dispatcher provide the minimum necessary source code implementation to complete a notepad software.

Therefore, through this example, you can also obtain content including:

> 1.Clean code style & standard naming conventions
>
> 2.In-depth understanding of “Reactive programming” knowledge points & correct use
>
> 3.Full use of AndroidX and Material Design
>
> 4.ConstraintLayout Constraint Layout Best Practices
>
> 5.Best Practices for Hex Compound State Management
>
> 6.Excellent User Experience & Interaction Design


&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

The icon material in the project comes from [iconfinder](https://www.iconfinder.com/) provided free licensed images.

&nbsp;

# Copyright

The scene cases and MVI dispatcher framework of this project are all my independent original designs, and I have the final right to interpret them.

If you need to quote and use the "background and ideas of the framework design of this project" for writing and publishing, please indicate the source of the link.

&nbsp;

# License

```
Copyright 2019-present KunMinX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```