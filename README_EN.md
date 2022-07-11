![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vz58k6asj218r0u0jwr.jpg)

&nbsp;

# Foreword

With the popularity of LiveData and ViewModel, developers also try to eliminate mutable boilerplate code by “local MVI pattern”.

According to the actual situation, most of the company’s “ancient giant projects” still need to upgrade and maintain Java, and Java is a frequent source of consistency problems, and “architectural components” are urgently needed to help eliminate hidden dangers.

Unfortunately, the current Android MVI articles or code cases are all written based on Kotlin language features, which are not friendly to Java, which makes Java developers interested in MVI mode in a dilemma.

The good news is that this issue comes with a well-polished MVI practice case.

|                          Collect or topped                   |                           Smooth transition                  |                           Delete notes                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vup9ck57g20u01o0hbm.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupfbex2g20u01o0qv6.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vuplwiuqg20u01o0x2t.gif) |

&nbsp;

# Project Description

I have been focusing on the “business architecture” model for a long time, and the open source components such as [UnPeekLiveData](https://github.com/KunMinX/UnPeek-LiveData) have been experienced in production environments such as QQ Music with over 100 million monthly lives.

In this case, I will show you how MVI-Dispatcher can simplify the otherwise “complicated and error-prone” message distribution process with just a few lines of code.

&nbsp;

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch:4.2.0-beta'
```
&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupvpzprj21o40h90wp.jpg)

&nbsp;

MVI-Dispatcher is applicable to Java, through which,

> 1.Can completely eliminate mutable boilerplate code, no need to write one line
>
> 2.Multiple events can be sent continuously to solve MVI scene LiveData event coverage
>
> 3.High performance fixed length queue, use as you go, run out of it, and never lose events
>
> 4.It can prevent team newbies from indiscriminately using mutableLiveData.setValue( ) in Activity/Fragment
>
> 5.Developers only need to pay attention to input and output, inject events from the unique entry input (), and observe at the unique exit output ()
>
> 6.If the new team members are not familiar with LiveData, UnPeekLiveData, mutable and MVI, they can also automatically realize the development of “one-way data flow” based on the simple and easy-to-understand I/O design of MVI-Dispatcher “single entry + single exit”
>
> 7.It can be seamlessly integrated into jetpack MVVM and other mode projects

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3zghmu13ej21j80u0tfn.jpg)

&nbsp;

In addition to mastering "Single Source of Truth" best practices in “Simple and Simple” code, you can also get content from this project including:

1.Clean code style & standard naming conventions

2.In-depth understanding of “view controller” knowledge points & correct use

3.Full use of AndroidX and Material Design

4.ConstraintLayout Constraint Layout Best Practices

5.Best Practices for Hex Compound State Management

6.Excellent User Experience & Interaction Design


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