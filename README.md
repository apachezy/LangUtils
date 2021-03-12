Language Utils
==============
A Bukkit/Spigot API to get the localized names of vanilla items, entitys, enchantments, biomes, potions, etc.

This project comes from [MascusJeoraly:LanguageUtils][Original].

Now, it has been completely refactored, improved performance, can support more vanilla object name translations, is
compatible with the previous LangUtils, and a copy can support all Minecraft versions above 1.13 at the same time.

**Tip:** If you want to use it on a server of 1.12.2 and below, please go to the original
project: [MascusJeoraly:LanguageUtils][Original]

### Features：

* Translate the name of the Item or Material.
* Translate the name of Biome.
* Translate the name of Entity.
* Translate the name of Enchantments.
* Translate the names of Potions, Potion Effects, and Tipped Arrows.
* Translate Tropical Fish Type(Pattern) names and Predefined Tropical Fish names.
* Translate the name of the Dye Color.
* Translate Villagers’ Level and Profession names.
* Translate the name of the Banner Pattern and the name of the colored Shield.
___
### Install

Please go to [Releases](https://github.com/apachezy/LangUtils/releases) to download the latest version, put it in your
server 'plugins' directory, and restart your server. Please delete all old LangUtils plugins before this.

### Configuration

This is an example:

```yaml
# Please do not remove this.
Extra-TAG: tag_r72EhIAL

# When a name in the language requested does not exist, the name will be
# retrieved in this language. The default fall back language is English.
FallbackLanguage: en_us

# If you want to enable the loading of a language, add it to the following list
# LoadLanguage: [ja_jp, ko_kr, ru_ru, zh_cn, zh_tw]
# Or
LoadLanguage:
  - ja_jp
  - ko_kr
  - ru_ru
  - zh_cn
  - zh_tw
# If you want to load all the languages, add "all" to the list:
# - all
```

___

### Use LangUtils in your plugin

1. **Declaring repositories and dependencies to your build tools**

* Build with Gradle</br>
  <font color=gray>Add a repository and a dependency to your `build.gradle`:</font>
  ```groovy
  repositories {
      maven {
          url 'https://jitpack.io'
      }
  }
  
  dependencies {
      // LangUtils
      provided group: 'com.github.apachezy', name: 'LangUtils', version: '3.1.3' // Please check the latest version
  }
  ```
* Build with Maven</br>
  <font color=gray>Add a repository and a dependency to your `pom.xml`:</font>
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
          <!--Please check the latest version -->  
          <version>3.1.3</version>
          <scope>provided</scope>
      </dependency>
  </dependencies>
  ```

2. **Add plugin dependencies to your plugin.yml**</br>

* If your plugin must depend on LangUtils to run:</br>
  Add this line to your `plugin.yml`: `depend: [..., LangUtils]`</br>
  <font color=gray>Where `...` are other plugins.</font>
* If your plugin does not depend on LangUtils, it can also run:</br>
  Add this line to your `plugin.yml`: `softdepend: [..., LangUtils]`</br>
  <font color=gray>Where `...` are other plugins.</font>

3. **Use LangUtils**</br>

* The best usage is to create a hook for LangUtils:</br>
  The hook may be static class, or be singleton, or be a parameterized instance, as you like.</br>
  <font color=gray>The following is an example of a hook using a static class:</font>
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
          hooked = plugin != null && plugin.isEnabled;
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
  
      // Add another method...
  }
  ```
* Then initialize the hook when your plugin starts:
  ```java
  package com.exampleplugin;
  
  import org.bukkit.plugin.java.JavaPlugin;
  
  public class MyPlugin extends JavaPlugin {
  
      @Override
      public void onEnable() {
          // Initialize LangUtilsHook
          LangUtilsHook.init();
      }
  
  }
  ```
* Call the hook where needed:
  ```java
  ItemStack item = new ItemStack(Material.STONE);
  // Call LangUtilsHook to get the name of the item.
  String name = LangUtilsHook.getItemName(item, "zh_cn");
  ```

___

### Contribute to this Project

If you find any problems or want to improve the efficiency, please send me PR or issues.


[Original]: https://github.com/MascusJeoraly/LanguageUtils
