LangUtils
=========
[![English](https://img.shields.io/badge/Lang-English-blue)](README.md) [![Chinese](https://img.shields.io/badge/语言-简体中文-green)](README_CN.md)

一个Bukkit/Spigot API，用于获取原版物品、实体、附魔、生物群系、药水等的本地化名称。

该项目来自[MascusJeoraly:LanguageUtils][Original].

它现在已经被完全重写，提高了性能、可以获取更多的原版对象名称、可以与以前的 LangUtils 兼容，并且一个副本可以同时支持所有高于 1.13 的Minecraft版本。

**提示：** 如果要在 1.12.2 及更低版本的服务器上使用，请访问原项目： [MascusJeoraly:LanguageUtils][Original]

### 功能：

* 获取物品和材料的名称。
* 获取生物群系的本地化名称。
* 获取实体的本地化名称。
* 获取附魔的本地化名称。
* 获取药水、药水效果和药箭的本地化名称。
* 获取热带鱼类型（图案）和预定义热带鱼的本地化名称。
* 获取染料颜色的本地化名称。
* 获取村民等级和专业的本地化名称。
* 获取旗帜图案和带颜色的盾牌本地化名称。

### 支持的 Minecraft 版本
* 1.13  1.14  1.15  1.16
___
### 安装

请到 [Releases](https://github.com/apachezy/LangUtils/releases) 下载最新版本，并放到您服务器的 “plugins” 目录中，然后重启服务器。此前请删除所有旧的 LangUtils 插件。

### 配置

这是一个配置范例：

```yaml
# 请不要修改或删除这一项，这是用来识别该插件的标志。
Extra-TAG: tag_r72EhIAL

# 后备语言。找不到请求的语言时，将使用后备语言来查找。
# 默认的后备语言是 en_us (美国英语)。
FallbackLanguage: en_us

# 如果您想要自定义加载语言，请按下列所示格式添加
# LoadLanguage: [ja_jp, ko_kr, ru_ru, zh_cn, zh_tw]
# 或：
LoadLanguage:
  - ja_jp
  - ko_kr
  - ru_ru
  - zh_cn
  - zh_tw
# 如果要想加载所有语言，可以添加 - all 到列表里
# - all
```

___

### 在您的插件里使用 LangUtils

1. **向您的构建工具声明仓库和依赖**

* ***用 Gradle 构建***</br>
  <font color=gray>在文件`build.gradle`增加仓库和依赖项：</font>
  ```groovy
  repositories {
      maven {
          url 'https://jitpack.io'
      }
  }
  
  dependencies {
      // 请检查最新版本
      provided group: 'com.github.apachezy', name: 'LangUtils', version: '3.1.3'
  }
  ```
* ***用 Maven 构建***</br>
  <font color=gray>在文件`pom.xml`里增加仓库和依赖项：</font>
  ```xml
  <repositories>
      <repository>
          <id>jitpack.io</id>
          <url>https://jitpack.io</url>
      </repository>
  </repositories>
  
  <dependencies>
      <!-- LangUtils -->
      <dependency>
          <groupId>com.github.apachezy</groupId>
          <artifactId>LangUtils</artifactId>
          <!-- 请检查最新版本 -->  
          <version>3.1.3</version>
          <scope>provided</scope>
      </dependency>
  </dependencies>
  ```

2. **在您的插件`plugin.yml`里声明依赖 LangUtils**</br>

* 如果您的插件需要 LangUtils 才能正常运行（硬依赖）：</br>
  在`plugin.yml`里加入依赖： `depend: [..., LangUtils]`</br>
  <font color=gray>此处`...`代表您的插件依赖的其它插件。</font>
* 如果您的插件没有 LangUtils 也能正常运行（软依赖）：</br>
  在`plugin.yml`里加入软依赖： `softdepend: [..., LangUtils]`</br>
  <font color=gray>此处`...`代表您的插件软依赖的其它插件。</font>

3. **使用 LangUtils**</br>

* 最佳用法是为 LangUtils 创建一个钩子：</br>
  钩子可以是静态类、单例或参数化实例，请根据您的使用习惯选择。</br>
  <font color=gray>以下展示一个静态类钩子：</font>
  ```java
  package com.exampleplugin.hooks;
  
  import com.meowj.langutils.lang.LanguageHelper;
  import org.bukkit.entity.Entity;
  import org.bukkit.inventory.ItemStack;
  import org.bukkit.plugin.Plugin;
  
  public class LangUtilsHook {
  
      private static boolean hooked;
  
      public static void init() {
          Plugin plugin = Bukkit.getPluginManager().getPlugin("LangUtils");
          hooked = plugin != null && plugin.isEnabled();
      }
  
      public static String getItemName(ItemStack itemStack, String locale) {
          if (hooked) {
              return LanguageHelper.getItemName(itemStack, locale);
          }
          return itemStack.getType().name();
      }
  
      public static String getEntityName(Entity entity, String locale) {
          if (hooked) {
              return LanguageHelper.getEntityName(entity, locale);
          }
          return entity.getName();
      }
  
      // 增加其他方法...
  }
  ```
* 在您的插件启动时初始化钩子：
  ```java
  package com.exampleplugin;
  
  import com.exampleplugin.hooks;
  import org.bukkit.plugin.java.JavaPlugin;
  
  public class MyPlugin extends JavaPlugin {
  
      @Override
      public void onEnable() {
          // 初始化钩子
          LangUtilsHook.init();
      }
  
  }
  ```
* 然后在需要的地方调用钩子：
  ```java
  ItemStack item = new ItemStack(Material.STONE);
  // 调用钩子获取物品的本地化名称
  String name = LangUtilsHook.getItemName(item, "zh_cn");
  ```

___

### 为该项目做贡献

如果您发现任何问题或想要提高效率，请给我发送PR或问题。


[Original]: https://github.com/MascusJeoraly/LanguageUtils
[English]: README.md
[Chinese]: README_CN.md
