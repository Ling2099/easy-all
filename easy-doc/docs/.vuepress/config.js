module.exports = {
  title: 'Easy-All 帮助文档',
  description: '别再为他人的屎山消耗自己的青春',
  base: '/easy-doc/',
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
        collapsable: true,
        sidebarDepth: 1,
        children: [
          '/view/page02/index01.md',
          '/view/page02/index02.md',
          '/view/page02/index03.md',
          '/view/page02/index04.md',
          '/view/page02/index05.md'
        ]
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
          { text: '安装使用', link: '/view/page02/index' },
          { text: '更新日志', link: '/view/page03/index' }
        ]
      },
      {
        text: '源码地址',
        items: [
          { text: 'Gitee', link: 'https://gitee.com/supersame/easy-all' },
          { text: 'GitHub', link: 'https://github.com/Ling2099/easy-all' }
        ]
      },
      { text: '更新日志', link: '/view/page03/index' },
      { text: 'MyBatis-Plus', link: 'https://baomidou.com/' },
      { text: 'Hutool', link: 'https://hutool.cn/docs/#/' }
    ]
  }
}