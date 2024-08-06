<template>
  <div>
    <!-- 使用 Element UI 的 Card 组件 -->
    <el-card style="width: 50%">
      <!-- 表单，用于展示和编辑用户信息 -->
      <el-form :model="user" label-width="100px" style="padding-right: 50px">
        <!-- 头像上传区域 -->
        <div style="margin: 15px; text-align: center">
          <el-upload class="avatar-uploader" :action="$baseUrl + '/files/upload'" <!-- 上传接口地址 -->
            :show-file-list="false" <!-- 不显示文件列表 -->
            :on-success="handleAvatarSuccess" <!-- 上传成功时的回调 -->
            >
            <!-- 如果有头像则显示头像，否则显示加号图标 -->
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <!-- 用户名显示，禁止编辑 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <!-- 姓名输入框 -->
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <!-- 电话输入框 -->
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <!-- 邮箱输入框 -->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <!-- 保存按钮 -->
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "AdminPerson", // 组件名称
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}') // 从本地存储中获取用户信息
    }
  },
  created() {
    // 组件创建时的生命周期钩子，可用于初始化数据
  },
  methods: {
    update() {
      // 保存当前的用户信息到数据库
      this.$request.put('/admin/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')

          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把 user 的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
  /* 设置表单标签字体加粗 */
}

/deep/.el-upload {
  border-radius: 50%;
  /* 设置上传组件的边框为圆形 */
}

/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  /* 设置上传组件的边框样式 */
  cursor: pointer;
  /* 设置鼠标指针为手型 */
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}

/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
  /* 设置上传组件悬停时的边框颜色 */
}

.avatar-uploader-icon {
  font-size: 28px;
  /* 设置上传图标的字体大小 */
  color: #8c939d;
  /* 设置上传图标的颜色 */
  width: 120px;
  /* 设置上传组件的宽度 */
  height: 120px;
  /* 设置上传组件的高度 */
  line-height: 120px;
  /* 设置行高 */
  text-align: center;
  /* 设置文本居中 */
  border-radius: 50%;
  /* 设置边框为圆形 */
}

.avatar {
  width: 120px;
  /* 设置头像图片的宽度 */
  height: 120px;
  /* 设置头像图片的高度 */
  display: block;
  /* 设置头像为块级元素 */
  border-radius: 50%;
  /* 设置头像为圆形 */
}
</style>
