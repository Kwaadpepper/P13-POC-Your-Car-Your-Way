import TailwindBootstrapGrid from 'tailwind-bootstrap-grid';
import PrimeUI from 'tailwindcss-primeui';

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./projects/**/*.{html,ts}"
  ],
  plugins: [
    PrimeUI,
    TailwindBootstrapGrid({
      container_max_widths: [
        'sm', '540px',
        'md', '720px',
        'lg', '960px',
        'xl', '1140px',
        '2xl', '1320px',
      ],
    }),
  ],
  corePlugins: {
    container: false,
  },
}
