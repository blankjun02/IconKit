# IconKit

IconKit 是一款 Android 应用图标查看工具，用于查看已安装应用的图标资源，了解自适应图标适配情况，并分享图标资源。

## 功能

- 查看和搜索已安装应用，支持按应用名称或包名排序，以及选择是否显示系统应用。
- 查看应用的 APK 图标资源与系统 API 返回的运行时图标。
- 以 APK 资源或系统 API 为判断依据，统计自适应与非自适应图标的数量和占比。
- 在图标详情页查看前景、背景等图层，并预览不同形状的蒙版效果。
- 长按选择应用，将 `appfilter.xml` 和 APK 中匹配的图标资源打包为 ZIP 并分享。
- 支持 MIUIX 和 Material 3 界面风格，以及明暗模式等主题设置。

图标获取结果可能因 Android 版本、设备厂商或应用资源配置而不同；无法读取的图标会被标记为不可用。

## 安装

需要 Android 7.0（API 24）或更高版本。可前往 [Releases](https://github.com/blankjun02/IconKit/releases) 查看可用安装包。

## 从源码构建

使用 Android Studio 打开项目，并配置 Android SDK Platform 37。项目使用 Gradle Wrapper（Gradle 9.5.0）；Gradle Daemon 的 JVM 版本配置为 25。首次构建需要下载 Gradle 和项目依赖。

在项目根目录执行：

```bash
# Windows
gradlew.bat :app:assembleDebug

# macOS / Linux
./gradlew :app:assembleDebug
```

Debug APK 输出在 `app/build/outputs/apk/debug/`。正式发布前请配置自己的签名密钥并使用签名后的 Release 构建。

## 权限说明

IconKit 使用 `QUERY_ALL_PACKAGES` 查询已安装应用，以实现图标查看和统计。分享操作通过 Android 分享面板由用户选择接收应用。

## 致谢

- [MIUIX](https://github.com/compose-miuix-ui/miuix)：界面组件库。
- [KernelSU Manager](https://github.com/tiann/KernelSU/)：界面与交互参考。
- [Boundo](https://github.com/LIU64N/Boundo)：图标详情页参考。
- [Donut](https://github.com/futuredapp/donut)：统计图表。

## 联系

- GitHub：[blankjun02/IconKit](https://github.com/blankjun02/IconKit)
- Telegram 频道：[@blacksoft_update](https://t.me/blacksoft_update)
