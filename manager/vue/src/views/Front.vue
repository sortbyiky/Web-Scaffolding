<template>
  <div>
    <!-- 公告栏 -->
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>公告：{{ top }}</div>
    <!-- 头部 -->
    <div class="front-header">
      <div class="front-header-left">
        <!-- 显示网站 logo -->
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">项目前台</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <!-- 使用 Element UI 的菜单组件 -->
          <el-menu :default-active="$route.path" mode="horizontal" router>
            <!-- 首页链接 -->
            <el-menu-item index="/front/home">首页</el-menu-item>
            <!-- 个人中心链接 -->
            <el-menu-item index="/front/person">个人中心</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="front-header-right">
        <!-- 未登录状态显示登录注册按钮 -->
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <!-- 登录状态显示用户信息和退出按钮 -->
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!-- 主体内容 -->
    <div class="main-body">
      <!-- 使用 router-view 组件渲染子路由 -->
      <router-view ref="child" @update:user="updateUser" />
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontLayout", // 组件名称

  data() {
    return {
      top: '', // 当前显示的公告内容
      notice: [], // 公告列表
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'), // 从本地存储获取用户信息
    }
  },

  mounted() {
    this.loadNotice() // 组件挂载后加载公告信息
  },

  methods: {
    loadNotice() {
      // 从服务器获取所有公告
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data // 存储公告数据
        let i = 0 // 公告索引
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content // 显示第一条公告内容
          setInterval(() => {
            this.top = this.notice[i].content // 切换公告内容
            i++
            if (i === this.notice.length) {
              i = 0 // 重新开始循环
            }
          }, 2500) // 每2.5秒切换一次公告
        }
      })
    },
    updateUser() {
      // 重新获取用户信息
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user"); // 移除本地存储的用户信息
      this.$router.push("/login"); // 跳转到登录页面
    },
  }
}
</script>

<style scoped>
@import "@/assets/css/front.css";
/* 引入外部样式文件 */
</style>
