export type UUID = `${string}-${string}-${string}-${string}-${string}` & { readonly __uuid: unique symbol }
