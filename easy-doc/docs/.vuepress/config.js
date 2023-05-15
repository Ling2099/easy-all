module.exports = {
  title: 'Easy-All 帮助文档',
  description: '别再为别人的屎山贡献自己的青春',
  head: [
    ['link', { rel: 'icon', href: 'logo.png' }]
  ],
  plugins: {
    '@vuepress/medium-zoom': {
      selector: 'img',
      options: {
        margin: 16
      }
    }
  },
  themeConfig: {
    sidebar: [
      {
        title: '快速开始',
        path: '/view/page01/',
        collapsable: false,
        sidebarDepth: 1
      },
      {
        title: '安装使用',
        path: '/view/start/',
        collapsable: false,
        sidebarDepth: 1
      },
      {
        title: '开发指南',
        path: '/view/dev/child',
        collapsable: true,
        sidebarDepth: 1,
        children: [
          '/view/dev/child/index01.md',              // 开发规范
          '/view/dev/child/index02.md',              // 版本命名
          '/view/dev/child/index03.md',              // 系统基础 API
          {
            title: '常用方法',
            path: '/view/dev/child/child/',
            collapsable: true,
            children: [
              '/view/dev/child/child/index04-01.md', // 获取当前用户信息
              '/view/dev/child/child/index04-02.md', // 获取客户端 HTTP 信息
              '/view/dev/child/child/index04-03.md', // Word、Excel 操作
              '/view/dev/child/child/index04-04.md', // 链式及 λ 表达式
            ]
          },
          '/view/dev/child/index05.md',              // Restful 接口
          '/view/dev/child/index06.md',              // 服务调用
          '/view/dev/child/index07.md',              // 任务调度
        ]
      },
      {
        title: '运维指南',
        path: '/view/test/',
        collapsable: false,
        sidebarDepth: 1
      },
      {
        title: '操作手册',
        path: '/view/prod/',
        collapsable: false,
        sidebarDepth: 1
      }
    ],
    logo: '/assets/img/claw.png',
    nav: [
      { text: '首页', link: '/' },
      {
        text: '指南',
        items: [
          { text: '快速开始', link: '/view/page01/index' },
          { text: '运维指南', link: '/view/test/index' },
          { text: '操作手册', link: '/view/prod/index' }
        ]
      },
      {
        text: '源码地址',
        items: [
          { text: 'Gitee', link: 'https://gitee.com/supersame/easy-all' },
          { text: 'GitHub', link: 'https://github.com/Ling2099/easy-all' }
        ]
      },
      { text: '更新日志', link: '/view/log/index' },
      { text: 'MyBatis-Plus', link: 'https://baomidou.com/' }
    ]
  }
}