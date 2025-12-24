@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<EcoAlternativeDTO> recommendAlternatives(Long cartId) {

        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        List<EcoAlternativeDTO> recommendations = new ArrayList<>();

        for (CartItem item : items) {
            Product p = item.getProduct();

            List<Product> alts =
                productRepository
                    .findTop3ByCategoryAndCarbonImpactKgLessThanAndIdNotOrderByCarbonImpactKgAsc(
                        p.getCategory(),
                        p.getCarbonImpactKg(),
                        p.getId()
                    );

            alts.forEach(a -> recommendations.add(
                EcoAlternativeDTO.builder()
                    .originalProductId(p.getId())
                    .alternativeProductId(a.getId())
                    .alternativeName(a.getName())
                    .price(a.getPrice())
                    .carbonImpactKg(a.getCarbonImpactKg())
                    .build()
            ));
        }
        return recommendations;
    }
}
