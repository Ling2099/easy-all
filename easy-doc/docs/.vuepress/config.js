module.exports = {
  title: 'Easy-All 帮助文档',
  description: '别再为他人的屎山消耗自己的青春',
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
        path: '/view/page02/',
        collapsable: false,
        sidebarDepth: 1
      },
      {
        title: '更新日志',
        path: '/view/page03/',
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
          { text: '安装使用', link: '/view/test/index' },
          { text: '更新日志', link: '/view/prod/index' }
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