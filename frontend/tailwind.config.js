/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{html,js}"],
    theme: {
        extend: {},
    },
    plugins: [
        require('tailwind-bootstrap-grid')({
            containerMaxWidths: {
                sm: '540px',
                md: '720px',
                lg: '960px',
                xl: '1140px',
            },
        }),
        require('tailwindcss-primeui'),
        require('@tailwindcss/typography'),
    ],
    corePlugins: {
        container: false,
    },
}
