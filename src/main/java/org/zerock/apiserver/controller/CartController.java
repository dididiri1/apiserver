package org.zerock.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.apiserver.dto.CartItemDTO;
import org.zerock.apiserver.dto.CartItemListDTO;
import org.zerock.apiserver.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody CartItemDTO itemDTO) {
        log.info(itemDTO);

        if(itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    private List<CartItemListDTO> getCartItems(Principal principal) {

        String email = principal.getName();

        return cartService.getCartItems(email);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    private List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {

        return cartService.remove(cino);

    }
}
