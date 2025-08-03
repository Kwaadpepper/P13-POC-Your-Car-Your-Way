import tailwindBootstrapGrid from 'tailwind-bootstrap-grid';
import tailwindcssPrimeUI from 'tailwindcss-primeui';

/** @type {import('tailwindcss').Config} */
export default {
  content: [
      "./projects/**/*.{html,ts}",
      "./projects/**/*.{css,scss}"
    ],
    theme: {
        extend: {},
    },
    plugins: [
        tailwindBootstrapGrid({
            containerMaxWidths: {
                sm: '540px',
                md: '720px',
                lg: '960px',
                xl: '1140px',
            },
        }),
        tailwindcssPrimeUI,
    ],
    corePlugins: {
        container: false,
    },
}
