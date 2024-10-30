<template>
<span class="Notification">
    <svg class="Notification" width="44" height="44" viewBox="0 0 44 44"
         fill="none"
         xmlns="http://www.w3.org/2000/svg">
    <circle cx="22" cy="22" r="22" fill="#382D3F" fill-opacity="0.5" />
    <path
      d="M15.7 28V19.1765C15.7 15.7653 18.5206 13 22 13C25.4794 13 28.3 15.7653 28.3 19.1765V28M13 28H31"
      stroke="#F7F7F7" stroke-width="1.5" stroke-linecap="round"
      stroke-linejoin="round" />
    <path
      d="M22 31C23.1046 31 24 29.8807 24 28.5V28H20V28.5C20 29.8807 20.8954 31 22 31Z"
      stroke="#F7F7F7" stroke-width="1.5" stroke-linecap="round"
      stroke-linejoin="round" />
    <rect x="22" y="5.71484" width="12" height="12" rx="6"
          fill="url(#paint0_linear_1669_7649)" />
    <defs>
      <linearGradient id="paint0_linear_1669_7649" x1="28" y1="5.71484" x2="28"
                      y2="17.7148" gradientUnits="userSpaceOnUse">
        <stop stop-color="#DC7AEF" />
        <stop offset="1" stop-color="#9282FF" />
      </linearGradient>
    </defs>
  </svg>
</span>

</template>

<script setup>

</script>

<style scoped lang="scss">

.Notification {
  min-width: 44px;
  min-height: 44px;
  max-width: 44px;
  max-height: 44px;

  svg {
    width: inherit;
    height: inherit;
    margin-left: 15px;
  }
}
</style>