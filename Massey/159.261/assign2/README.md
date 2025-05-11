梳理游戏流程：
我们已经有一个基础的游戏引擎类，该类有如下方法

数学方法
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510150432521.png)

绘图方法
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510150506105.png)

资源加载
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510150618208.png)

接口
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510150712674.png)

监听
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510150738414.png)

该类是最底层的游戏引擎，继承该类只需要重写绘制逻辑即可。其中的     
`paintCompoent()`方法可视为绘制那些对象？
`update()` 可视为怎样绘制对象？

游戏引擎是一个接口/抽象类，其目的是要求游戏主类继承游戏引擎并重写其中绘制方法（需要绘制那些对象/如何绘制），因此我们需要创建一个游戏主类来协调所有游戏的对象 `GameController` 该对象继承引擎，因此可视为引擎的实现类

游戏主类/游戏控制器的职责如下
- 新建所有对象
- 控制协调所有对象
- 调用对象的更新方法
- 负责资源的加载

在讲游戏引擎如何使用之前，先来说游戏中的所有对象有什么属性和方法
首先敌人类

敌人在游戏中的作用是，随节拍移动并接近玩家，攻击玩家。目标是杀死玩家。因此敌人类需要

- 一个全局的节拍器来控制移动时机。这个节拍器需要==保证全局单例==。
-  当前坐标来计算路径。
- 血量
- 攻击力

  ![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510153820369.png)

- 动画/绘制方式
- 声音/打击反馈

前三个没什么可说的，主要是动画和声音。因为所有实现类都有自己的音效和动画。所以需要写一个抽象方法来实现多态

动画和音效方法的逻辑如下
- 游戏主类调用地图类更新
- 地图类调用所有实体的新建/更新逻辑
- 敌人根据当前状态调用相应绘图逻辑

控制器和地图一会再说，先来说敌人类的更新方法
- 判断敌人当前状态
- 执行对应的动画/声音（哈希表）（更新策略）
  与其他类的交互/生命周期等在地图处理，以节拍为更新信号。最后在控制器绘制

我们尝试创建一只史莱姆 `Slime`
首先继承 `Enemy`
为了绘制动画，我们需要数组来存储动画帧

![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510202332606.png)

所有资源的加载都应该在控制器进行，所以




写了史莱姆的攻击动画
问题：Enemy（敌人）类的代码冗余，史莱姆或者其他实体继承敌人类后应该只重写攻击方式和动画，其他直接复用敌人类代码。需要修改敌人类和史莱姆的攻击方法
目前的敌人类逻辑是：
敌人类初始化：
敌人状态：
![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510144204314.png)

敌人实体在与玩家交互时会在上述6种状态间切换。所有敌人实体目前均只有这6种状态。

敌人状态会与敌人动画绑定，根据不同的状态来调用不同的sprite，使用哈希表来做状态绑定。所有敌人实体都需要这个属性，写到父类里面做多态

![image.png](https://cdn.jsdelivr.net/gh/hamhuo-hub/HamPic@img/img/20250510145208594.png)

我们不希望所有子类共享一个哈希表，所以在子类重写一个哈希表覆盖

