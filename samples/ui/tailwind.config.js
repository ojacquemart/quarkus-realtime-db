const defaultTheme = require('tailwindcss/defaultTheme')
const colors = require('tailwindcss/colors')

module.exports = {
  mode: 'jit',
  purge: {
    enabled: process.env.NODE_ENV === 'production',
    content: ['./index.html', './src/**/*.{vue,ts}'],
  },
  theme: {
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: colors.black,
      white: colors.white,
      gray: colors.trueGray,
      indigo: colors.indigo,
      red: colors.rose,
      yellow: colors.yellow,
      'primary': '#1b262c',
      'primary-light': '#3282b8',
      'secondary': '#0f4c75',
      'secondary-light': '#bbe1fa',
    },
    container: {
      center: true,
    },
    gridAutoRows: {
      '8': '8em',
    },
    gridTemplateRows: {
      'layout-3': 'auto 1fr auto',
    },
    gridTemplateColumns: {
      'collection-view': '15em 1fr',
    },
  },
  plugins: [
    require('@tailwindcss/aspect-ratio'),
    require('@tailwindcss/line-clamp'),
    require('@tailwindcss/typography'),
    require('@tailwindcss/forms'),
  ],
}
