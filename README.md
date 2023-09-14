![](https://s2.loli.net/2023/05/18/XdHUZuxiD3g5NsE.jpg)

&nbsp;

&nbsp;

### [🌏 English README](https://github.com/KunMinX/MVI-Dispatcher/blob/main/README_EN.md)

研发故事：[《解决 MVI 架构实战痛点》](https://juejin.cn/post/7134594010642907149)

&nbsp;

# 背景

响应式编程便于单元测试，但其自身存在漏洞，MVI 即是来消除漏洞，

MVI 有一定门槛，实现较繁琐，且存在性能等问题，难免同事撂挑子不干，一夜回到解放前，

综合来说，MVI 适合与 Jetpack Compose 搭配实现 “现代化的开发模式”，

反之如追求 “低成本、复用、稳定”，可通过遵循 “单一职责原则” 从源头把问题消除。

MVI-Dispatcher 应运而生。

&nbsp;

&nbsp;

|                          收藏或置顶                          |                           顺滑转场                           |                           删除笔记                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://images.xiaozhuanlan.com/photo/2022/3555d17b46e04054154916d00f1214f8.gif) | ![](https://images.xiaozhuanlan.com/photo/2022/d20a18e90cda8aa1f7d6977dca7b7135.gif) | ![](https://images.xiaozhuanlan.com/photo/2022/5786c16f17612661b0b490dd40e78608.gif) |

&nbsp;



# 项目简介

笔者长期专注 “业务架构” 模式，致力消除敏捷开发过程中 “不可预期问题”。

在本案例中，我将为您展示，MVI-Dispatcher 是如何将原本 "繁杂易出错" 消息分发流程，通过 **寥寥几行代码** 轻而易举完成。

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch:7.6.0'

//可选分支，简便安全完成 Config 读写
implementation 'com.kunminx.arch:keyvalue-dispatch:7.6.0'
```

&nbsp;

一个完备的 “领域层” 消息分发组件，至少应满足以下几点：

1.内含消息队列，可暂存 “发送过且未消费” 的消息，

2.页面不可见时，队列暂存期间发来的消息，页面重新可见时，自动消费未消费的消息。

MVI-Dispatcher 应运而生，

&nbsp;

此外，MVI-Dispatcher 改进和优化还包括：

> 1.**可彻底消除 mutable 样板代码**，一行不必写
>
> 2.**可杜绝团队新手滥用** mutable.setValue( ) 于 Activity/Fragment
>
> 3.开发者只需关注 input、output 二处，**从唯一入口 input 发起请求，并于唯一出口 output 观察**
>
> 4.团队新手在不熟 LiveData、UnPeekLiveData、SharedFlow、mutable、MVI 情况下，仅根据 MVI-Dispatcher 简明易懂 input-output 设计亦可自动实现 “响应式” 开发
>
> 5.可无缝整合至 Jetpack MVVM 等模式项目

&nbsp;

![](https://s2.loli.net/2023/05/18/mn2zeTJdqrlNw6P.jpg)

&nbsp;

MVI-Dispatcher 以 “备忘录场景” 为例，提供完成一款 “记事本软件” 最少必要源码实现，

故通过该示例，您还可获得内容包括：

> 1.整洁代码风格 & 标准命名规范
>
> 2.对 “响应式编程” 知识点深入理解 & 正确使用
>
> 3.AndroidX 和 Material Design 全面使用
>
> 4.ConstraintLayout 约束布局使用
>
> 5.**十六进制复合状态管理最佳实践**
>
> 6.优秀用户体验 & 交互设计

&nbsp;

# Thanks to

感谢小伙伴浓咖啡、苏旗的测试反馈

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

项目中图标素材来自 [iconfinder](https://www.iconfinder.com/) 提供 **免费授权图片**。

&nbsp;

# Copyright

本项目场景案例及 MVI-Dispatcher 框架，均属本人独立原创设计，本人对此享有最终解释权。

任何个人或组织，未经与作者本人当面沟通许可，不得将本项目代码设计及本人对 "响应式编程漏洞和 MVI" 独家理解用于 "**打包贩卖、出书、卖课**" 等商业用途。

如需引用借鉴 “本项目框架设计背景及思路” 写作发行，请注明**链接出处**。

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