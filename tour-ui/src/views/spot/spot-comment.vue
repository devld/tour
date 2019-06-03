<template>
  <div v-loading="loading" class="spot-comment-view">
    <div class="comments">
      <div class="comment" v-for="c in comments" :key="c.id">
        <user-view :user="c.author">
          <span class="time">{{ c.createdAt | timeFormatter }}</span>
        </user-view>
        <div class="content">{{ c.content }}</div>
        <div class="extra">
          <div class="delete">
            <i
              class="el-icon-delete"
              title="删除"
              v-if="userId === c.authorId || isAdmin"
              @click="deleteComment(c.id)"
            />
          </div>
          <div class="like">
            <i
              class="iconfont yes"
              title="取消点赞"
              v-if="c.liked"
              @click="toggleCommentLike(c, false)"
            >&#xe606;</i>
            <i class="iconfont no" v-else title="点赞" @click="toggleCommentLike(c, true)">&#xe8c4;</i>
            <span class="count">{{ c.likeCount }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="load-error" v-if="loadError">
      <span @click="loadComments">评论加载失败，点击重试</span>
    </div>
    <div class="page-param">
      <el-pagination
        :total="totalComments"
        :current-page="pageParam.page"
        :page-size="pageParam.pageSize"
        @current-change="loadComments"
      ></el-pagination>
      <el-select v-model="pageParam.sort" size="mini" @input="loadComments">
        <el-option label="点赞最多" value="like"/>
        <el-option label="最新发表" value="time"/>
      </el-select>
    </div>
    <div class="add-comment">
      <el-input
        v-model="commentContent"
        :maxlength="1000"
        :rows="4"
        type="textarea"
        :disabled="!login"
      />
      <el-button
        type="primary"
        class="iconfont send-button"
        size="small"
        :disabled="!commentContent.trim()"
        circle
        icon="iconsend"
        @click="addComment"
      />
      <div class="ask-login" v-if="!login">
        <span class="link" @click="showLogin">登录</span>
        <span>后评论</span>
      </div>
    </div>
  </div>
</template>
<script>
import { getSpotComments, toggleSpotCommentLike, commentSpot, deleteSpotComment } from '../../api/spot'
import UserView from '../user/user-view'

export default {
  name: 'SpotCommentView',
  components: { UserView },
  props: {
    spotId: {
      type: Number,
      required: true
    },
    userId: {
      type: Number
    },
    userType: {
      type: Number
    }
  },
  computed: {
    login () {
      return !!this.userId
    },
    isAdmin () {
      return this.userType === 1
    }
  },
  data () {
    return {
      loading: false,

      totalComments: 0,
      comments: [],
      loadError: null,

      commentContent: '',

      pageParam: {
        page: 1,
        pageSize: 5,
        sort: 'like'
      }
    }
  },
  mounted () {
    this.loadComments()
  },
  methods: {
    loadComments (page) {
      this.pageParam.page = typeof (page) === 'number' ? page : 1
      this.loading = true
      getSpotComments(this.spotId, this.pageParam).then(pageData => {
        this.pageParam.page = pageData.page
        this.totalComments = pageData.total
        pageData.data.forEach(e => {
          e.loading = false
        })
        this.comments = pageData.data
        this.loadError = null
      }, e => {
        this.$message.error('加载评论失败' + e.message)
        this.loadError = e
      }).then(() => {
        this.loading = false
      })
    },
    addComment () {
      this.loading = true
      commentSpot(this.spotId, this.commentContent.trim()).then(() => {
        this.commentContent = ''
        this.pageParam.sort = 'time'
        this.pageParam.page = 1
        this.loadComments()
      }, e => {
        this.$message.error('发表失败, ' + e.message)
      }).then(() => {
        this.loading = false
      })
    },
    toggleCommentLike (comment, like) {
      if (comment.loading) {
        return
      }
      comment.loading = true
      toggleSpotCommentLike(comment.id, like).then(() => {
        comment.liked = like
        comment.likeCount += like ? 1 : -1
      }, e => {
        console.error(e)
      }).then(() => {
        comment.loading = false
      })
    },
    deleteComment (commentId) {
      this.$confirm('确认删除这条评论？', '删除').then(() => {
        this.loading = true
        deleteSpotComment(commentId).then(() => {
          this.$message.success('删除成功')
          this.loadComments(this.pageParam.page)
        }, e => {
          this.$message.error(e.message)
        }).then(() => {
          this.loading = false
        })
      }, () => { })
    },
    showLogin () {
      Tour.Auth.showAuthDialog()
    }
  }
}
</script>
<style lang="scss" scoped>
.spot-comment-view {
  .comments {
    .comment {
      padding: 10px;
      background-color: #fff;
      margin: 10px 0;
      position: relative;

      .content {
        margin-top: 10px;
      }

      .time {
        color: gray;
      }

      .extra {
        user-select: none;
        position: absolute;
        top: 20px;
        right: 20px;

        & > div {
          display: inline-block;
        }

        .like {
          & > i {
            cursor: pointer;
          }

          .yes {
            color: rgb(255, 64, 0);
          }
        }

        .delete {
          i {
            color: red;
          }
        }
      }
    }
  }

  .load-error {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #aaa;
  }

  .add-comment {
    position: relative;
    margin-top: 10px;

    .ask-login {
      position: absolute;
      top: calc(50% - 10px);
      left: calc(50% - 40px);
      font-size: 0;
      & > span {
        font-size: 16px;
      }

      .link {
        cursor: pointer;
      }
    }

    .send-button {
      position: absolute;
      bottom: 10px;
      right: 10px;
    }
  }

  .link {
    color: skyblue;
  }

  .page-param {
    .el-pagination {
      display: inline-block;
      width: calc(100% - 106px);
    }
    .el-select {
      width: 100px;
    }
  }
}
</style>
