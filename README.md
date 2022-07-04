![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3v14d8u74j218z0u0te9.jpg)

&nbsp;

# 背景

随着 LiveData、ViewModel 普及，开发者亦尝试 “局部 MVI 模式” 消除 mutable 样板代码。

根据现实情况，多数公司 “远古巨型项目” 仍需 Java 升级维护，且 Java 恰是一致性问题频发大户，亟待架构组件相助，将隐患扼杀在摇篮。

只可惜，当下 Android MVI 网文或代码案例皆基于 Kotlin 语言特性编写，对 Java 不算友好，这使 MVI 模式感兴趣 Java 开发者左右为难。

好消息是，这一期我们带着精心打磨 Jetpack MVI 实践案例而来。

|                          收藏或置顶                          |                           顺滑转场                           |                           删除笔记                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3v2edejg3g20u01o01ju.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3v2eeqf6mg20u01o04qq.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3v2ecrr0gg20u01o01kx.gif) |

&nbsp;

# 项目简介

本人长期专注 “业务架构” 模式，所开源 [UnPeekLiveData](https://github.com/KunMinX/UnPeek-LiveData) 等组件已经过 QQ 音乐等月活过亿生产环境历练。

在本案例中，我将为你展示，Jetpack MVI 是如何 **以简驭繁** 将原本繁杂易出错之消息分发流程，通过 **寥寥几行代码** 轻而易举完成。

&nbsp;

该项目核心组件 MVI-Dispatcher 适用 Java，通过它，

> 1.**可连续发送多类型事件**，解决 MVI 场景 LiveData 事件覆盖
>
> 2.**彻底消除 mutable 样板代码**，一行也不用写

> 3.屏蔽内嵌 LiveData，杜绝团队新手于 Activity/Fragment 滥用 mutableLiveData.setValue( )

> 4.项目可移除 LiveData 或 UnPeekLiveData 依赖，杜绝团队新手误用

> 5.团队新手甚至无需知道 LiveData、UnPeekLiveData、mutable，根据 MVI-Dispatcher 简明易懂 **“唯一入口 + 唯一出口” I/O 设计** 即可自动实现 “单向数据流” 开发

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3v3fxvc8ij21o90hk42i.jpg)

&nbsp;

除 **在 “以简驭繁” 代码中掌握 Jetpack MVI 最佳实践**，你还可从该项目获得内容包括：

1.整洁代码风格 & 标准命名规范

2.对 “视图控制器” 知识点深入理解 & 正确使用

3.AndroidX 和 Material Design 全面使用

4.ConstraintLayout 约束布局最佳实践

5.十六进制复合状态管理最佳实践

6.**优秀用户体验 & 交互设计**

7.The one more thing is：

即日起，可于应用商店下载体验

&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

项目中图标素材来自 [iconfinder](https://www.iconfinder.com/) 提供 **免费授权图片**。

&nbsp;

# Copyright

本项目场景案例及 MVI-Dispatcher 框架，均属本人独立原创设计，本人对此享有最终解释权。

任何个人或组织，未经与作者本人当面沟通，不得将本项目代码设计及本人对 Jetpack MVI 独家理解用于 "**打包贩卖、引流、出书、卖课**" 等商业用途。

如需引用借鉴 “本项目框架设计背景及思路” 写作发行，须注明链接出处。

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
