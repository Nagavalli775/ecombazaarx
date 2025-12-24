@Service
@RequiredArgsConstructor
public List<EcoAlternativeDTO> recommendAlternatives(Long cartId) {

    List<CartItem> items = cartItemRepository.findByCartId(cartId);
    List<EcoAlternativeDTO> results = new ArrayList<>();

    for (CartItem item : items) {

        Product p = item.getProduct();

        List<Product> alternatives =
                productRepository
                        .findTop3ByCategoryAndCarbonImpactKgLessThanAndIdNotOrderByCarbonImpactKgAsc(
                                p.getCategory(),
                                p.getCarbonImpactKg(),
                                p.getId()
                        );

        if (alternatives.isEmpty()) {
            // Explicitly mark as best choice
            results.add(
                EcoAlternativeDTO.builder()
                    .originalProductId(p.getId())
                    .alternativeProductId(null)
                    .alternativeName("Best low-impact choice already selected ✔")
                    .price(p.getPrice())
                    .carbonImpactKg(p.getCarbonImpactKg())
                    .build()
            );
        } else {
            for (Product alt : alternatives) {
                results.add(
                    EcoAlternativeDTO.builder()
                        .originalProductId(p.getId())
                        .alternativeProductId(alt.getId())
                        .alternativeName(alt.getName())
                        .price(alt.getPrice())
                        .carbonImpactKg(alt.getCarbonImpactKg())
                        .build()
                );
            }
        }
    }
    return results;
}
