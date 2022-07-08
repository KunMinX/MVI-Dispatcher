![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vz58k6asj218r0u0jwr.jpg)

&nbsp;

# 背景

随着 LiveData、ViewModel 普及，开发者亦尝试 “局部 MVI 模式” 消除 mutable 样板代码。

根据现实情况，多数公司 “远古巨型项目” 仍需 Java 升级维护，且 Java 恰是一致性问题频发大户，亟待 "架构组件" 助力消除隐患。

只可惜，当下 Android MVI 网文或代码案例 皆基于 Kotlin 语言特性编写，对 Java 不算友好，这使 MVI 模式感兴趣 Java 开发者左右为难。

好消息是，这一期我们带着精心打磨 MVI 实践案例而来。

&nbsp;

|                          收藏或置顶                          |                           顺滑转场                           |                           删除笔记                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vup9ck57g20u01o0hbm.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupfbex2g20u01o0qv6.gif) | ![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vuplwiuqg20u01o0x2t.gif) |

&nbsp;

# 项目简介

本人长期专注 “业务架构” 模式，所开源 [UnPeekLiveData](https://github.com/KunMinX/UnPeek-LiveData) 等组件已经过 QQ 音乐等月活过亿生产环境历练。

在本案例中，我将为你展示，MVI-Dispatcher 是如何 **以简驭繁** 将原本 "繁杂易出错" 之消息分发流程，通过 **寥寥几行代码** 轻而易举完成。

&nbsp;

```Groovy
implementation 'com.kunminx.arch:mvi-dispatch:4.2.0-beta'
```

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3vupvpzprj21o40h90wp.jpg)

&nbsp;

亲爱的 MVI-Dispatcher，你已是个成熟的 '唯一可信源'，该学会自己去完成以下几点：

> 1.**可彻底消除 mutable 样板代码**，一行不必写
>
> 2.**可连续发送多事件**，解决 MVI 场景 LiveData 事件覆盖
>
> 3.**高性能定长队列，随取随用，用完即走，绝不丢失事件**
>
> 4.**可杜绝团队新手滥用** mutableLiveData.setValue( ) 于 Activity/Fragment
>
> 5.可移除 LiveData 或 UnPeekLiveData 依赖，杜绝团队新手误用
>
> 6.团队新手在不熟 LiveData、UnPeekLiveData、mutable、MVI 情况下，仅根据 MVI-Dispatcher 简明易懂 input-output 设计亦可自动实现 “单向数据流” 开发

&nbsp;

![](https://tva1.sinaimg.cn/large/e6c9d24ely1h3zghmu13ej21j80u0tfn.jpg)

&nbsp;

除 **在 “以简驭繁” 代码中掌握 “唯一可信源” 最佳实践**，你还可从该项目获得内容包括：

1.整洁代码风格 & 标准命名规范

2.对 “视图控制器” 知识点深入理解 & 正确使用

3.AndroidX 和 Material Design 全面使用

4.ConstraintLayout 约束布局使用

5.**十六进制复合状态管理最佳实践**

6.优秀用户体验 & 交互设计

&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack](https://developer.android.google.cn/jetpack/)

[SwipeDelMenuLayout](https://github.com/mcxtzhang/SwipeDelMenuLayout)

项目中图标素材来自 [iconfinder](https://www.iconfinder.com/) 提供 **免费授权图片**。

&nbsp;

# Copyright

本项目场景案例及 MVI-Dispatcher 框架，均属本人独立原创设计，本人对此享有最终解释权。

任何个人或组织，未经与作者本人当面沟通，不得将本项目代码设计及本人对 “唯一可信源” 及 MVI 独家理解用于 "**打包贩卖、引流、出书、卖课**" 等商业用途。

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