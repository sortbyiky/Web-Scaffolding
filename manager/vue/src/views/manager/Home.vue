<template>
  <div>
    <!-- 欢迎信息卡片 -->
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <!-- 公告列表 -->
    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">公告列表</div>
        <div>
          <!-- 使用 Element UI 的时间轴组件显示公告 -->
          <el-timeline reverse slot="reference">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time">
              <!-- 使用弹出框组件显示公告内容 -->
              <el-popover placement="right" width="200" trigger="hover" :content="item.content">
                <span slot="reference">{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home', // 组件名称
  data() {
    return {
      // 从本地存储中获取当前用户信息
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [] // 用于存储公告列表
    }
  },
  created() {
    // 组件创建时从服务器获取公告信息
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || [] // 更新公告列表数据
    })
  }
}
</script>
