# tour
旅游景点系统

## Setup

### 打包前端

```bash
cd tour-ui/
# 安装依赖
npm install
# 打包前端工程
npm run build
# 复制打包好的 tour.js 到后端工程中
cp dist/tour.js ../src/main/resources/static/js
```

### 后端配置

修改 `src/main/resources/application-test.yml` 中的相关配置。
其中需要配置七牛云的文件上传服务和高德地图的 Web API 的 key。

```bash
# 打包
mvn clean package
# 使用 `test` profile 启动
java -jar target/tour-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

默认端口为 `8822`
